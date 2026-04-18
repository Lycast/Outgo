package fr.abknative.outgo.wallet.impl.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import fr.abknative.outgo.auth.api.provider.SessionProvider
import fr.abknative.outgo.core.api.AppDispatchers
import fr.abknative.outgo.core.api.EpochMillis
import fr.abknative.outgo.core.api.IdProvider
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.core.api.logs.*
import fr.abknative.outgo.core.api.model.SyncStatus
import fr.abknative.outgo.database.OperationEntity
import fr.abknative.outgo.database.OutgoDatabase
import fr.abknative.outgo.wallet.api.model.Operation
import fr.abknative.outgo.wallet.api.repository.OperationRepository
import fr.abknative.outgo.wallet.impl.mapper.toDomain
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * SQLDelight implementation of [OperationRepository].
 *
 * Uses [OutgoDatabase] as the local source of truth.
 * Enforces data isolation by scoping all queries to the current user's ID.
 */
internal class OperationRepositoryImpl(
    private val database: OutgoDatabase,
    private val dispatchers: AppDispatchers,
    private val timeProvider: TimeProvider,
    private val idProvider: IdProvider,
    private val sessionProvider: SessionProvider
) : OperationRepository {

    private val queries = database.operationQueries
    private val tag = "OperationLocalRepo"

    private val currentUserId: String
        get() = sessionProvider.getCurrentUserId()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeOperationsByPeriod(walletId: String, from: EpochMillis, to: EpochMillis): Flow<List<Operation>> {
        return sessionProvider.observeUserId()
            .flatMapLatest { uid ->
                queries.getOperationsByPeriod(walletId = walletId, from = from, to = to, userId = uid)
                    .asFlow()
                    .mapToList(dispatchers.io)
                    .map { entities -> entities.map { it.toDomain() } }
                    .distinctUntilChanged()
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeAllOperations(walletId: String): Flow<List<Operation>> {
        return sessionProvider.observeUserId()
            .flatMapLatest { uid ->
                queries.getAllActiveOperations(walletId = walletId, userId = uid)
                    .asFlow()
                    .mapToList(dispatchers.io)
                    .map { entities -> entities.map { it.toDomain() } }
                    .distinctUntilChanged()
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observePendingOperations(): Flow<List<Operation>> {
        return sessionProvider.observeUserId()
            .flatMapLatest { uid ->
                queries.getPendingOperations(userId = uid)
                    .asFlow()
                    .mapToList(dispatchers.io)
                    .map { entities -> entities.map { it.toDomain() } }
            }
    }

    override suspend fun getOperationById(id: String): Operation? = withContext(dispatchers.io) {
        queries.getOperationById(id = id, userId = currentUserId).executeAsOneOrNull()?.toDomain()
    }

    override suspend fun save(operation: Operation): Result<Unit, AppException> = withContext(dispatchers.io) {
        asResult(
            onError = { e ->
                AppLogger.get()?.e(tag, "Failed to save operation: ${operation.id}", e)
                CommonError.DatabaseError(e)
            }
        ) {
            queries.transaction {
                val now = timeProvider.now()
                val uid = currentUserId
                val existing = queries.getOperationById(operation.id, uid).executeAsOneOrNull()

                if (existing == null) {
                    queries.insertFromDomain(operation, uid, SyncStatus.PENDING_CREATE, now, now, idProvider)
                } else if (existing.hasChanged(operation)) {
                    val nextStatus = SyncStatus.fromString(determineNextStatus(existing.syncStatus))
                    queries.updateFromDomain(operation, uid, nextStatus, now, existing.deletedAt)
                }
            }
        }
    }

    override suspend fun markAsDeleted(id: String): Result<Unit, AppException> = withContext(dispatchers.io) {
        asResult(
            onError = {
                AppLogger.get()?.e(tag, "Failed to mark operation as deleted: $id", it)
                CommonError.DatabaseError(it)
            }
        ) {
            queries.transaction {
                val uid = currentUserId
                val current = queries.getOperationById(id = id, userId = uid).executeAsOneOrNull()

                if (current?.syncStatus == SyncStatus.PENDING_CREATE.name) {
                    queries.hardDeletePendingCreate(id = id, userId = uid)
                } else {
                    queries.markAsDeleted(
                        deletedAt = timeProvider.now(),
                        updatedAt = timeProvider.now(),
                        id = id,
                        userId = uid
                    )
                }
            }
        }
    }

    override suspend fun getPendingOperations(): Result<List<Operation>, AppException> = withContext(dispatchers.io) {
        asResult(
            onError = {
                AppLogger.get()?.e(tag, "Failed to fetch pending operations", it)
                CommonError.DatabaseError(it)
            }
        ) {
            queries.getPendingOperations(userId = currentUserId).executeAsList().map { it.toDomain() }
        }
    }

    override suspend fun updateSyncStatus(
        id: String,
        status: SyncStatus
    ): Result<Unit, AppException> = withContext(dispatchers.io) {
        asResult(
            onError = {
                AppLogger.get()?.e(tag, "Failed to update sync status ($status) for operation: $id", it)
                CommonError.DatabaseError(it)
            }
        ) {
            queries.updateSyncStatus(syncStatus = status.name, id = id, userId = currentUserId)
            Unit
        }
    }

    override suspend fun syncFromServer(operations: List<Operation>): Result<Unit, AppException> = withContext(dispatchers.io) {
        asResult(
            onError = { e ->
                AppLogger.get()?.e(tag, "Failed to sync ${operations.size} operations", e)
                CommonError.DatabaseError(e)
            }
        ) {
            queries.transaction {
                val uid = currentUserId
                operations.forEach { remote ->
                    val local = queries.getOperationById(remote.id, uid).executeAsOneOrNull()

                    try {
                        if (local == null) {
                            AppLogger.get()?.d(tag, "-> Trying insert (ID: ${remote.id}) for active user: $uid")
                            queries.insertFromDomain(remote, uid, SyncStatus.SYNCED, remote.createdAt, remote.updatedAt, idProvider)
                        } else {
                            val isServerNewer = remote.updatedAt > local.updatedAt
                            val isSynced = SyncStatus.fromString(local.syncStatus) == SyncStatus.SYNCED

                            if (isSynced || isServerNewer) {
                                AppLogger.get()?.d(tag, "-> Trying update (ID: ${remote.id}) pour l'utilisateur Actif: $uid")
                                queries.updateFromDomain(remote, uid, SyncStatus.SYNCED, remote.updatedAt, remote.deletedAt)
                            }
                        }
                    } catch (e: Exception) {
                        AppLogger.get()?.e(tag, "Sync operation failed : ${remote.id} | name: ${remote.name} | User target: $uid", e)
                        throw e
                    }
                }
            }
        }
    }

    override suspend fun deleteAll(userId: String?): Result<Unit, AppException> = withContext(dispatchers.io) {
        asResult(
            onError = {
                AppLogger.get()?.e(tag, "Failed to delete all operations", it)
                CommonError.DatabaseError(it)
            }
        ) {
            if (userId != null) {
                queries.deleteAllForUser(userId = userId)
            } else {
                queries.deleteAll()
            }
            Unit
        }
    }

    // ==========================================
    // PRIVATE HELPERS
    // ==========================================

    /**
     * Checks if any domain field differs from the database entity.
     */
    private fun OperationEntity.hasChanged(domain: Operation): Boolean {
        return name != domain.name ||
                amountInCents != domain.amountInCents ||
                startDate != domain.startDate ||
                endDate != domain.endDate ||
                type != domain.type.name ||
                recurrence != domain.recurrence.name
    }

    /**
     * Determines the next sync status when updating an existing operation.
     */
    private fun determineNextStatus(currentStatusStr: String): String {
        val currentStatus = SyncStatus.fromString(currentStatusStr)
        return if (currentStatus == SyncStatus.PENDING_CREATE) {
            SyncStatus.PENDING_CREATE.name
        } else {
            SyncStatus.PENDING_UPDATE.name
        }
    }
}