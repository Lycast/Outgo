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

    override suspend fun getOperationById(id: String): Operation? = withContext(dispatchers.io) {
        queries.getOperationById(id = id, userId = currentUserId).executeAsOneOrNull()?.toDomain()
    }

    override suspend fun save(operation: Operation): Result<Unit, AppException> = withContext(dispatchers.io) {
        asResult(
            onError = {
                AppLogger.get()?.e(tag, "Failed to save operation: ${operation.id}", it)
                CommonError.DatabaseError(it)
            }
        ) {
            queries.transaction {
                val now = timeProvider.now()
                val uid = currentUserId
                val existing = queries.getOperationById(id = operation.id, userId = uid).executeAsOneOrNull()

                if (existing == null) {
                    val finalId = operation.id.ifBlank { idProvider.generate() }
                    queries.insertOperation(
                        id = finalId,
                        userId = uid,
                        walletId = operation.walletId,
                        name = operation.name,
                        amountInCents = operation.amountInCents,
                        type = operation.type.name,
                        recurrence = operation.recurrence.name,
                        startDate = operation.startDate,
                        endDate = operation.endDate,
                        createdAt = now,
                        updatedAt = now,
                        deletedAt = null,
                        syncStatus = SyncStatus.PENDING_CREATE.name
                    )
                } else if (existing.hasChanged(operation)) {
                    queries.updateOperation(
                        walletId = operation.walletId,
                        name = operation.name,
                        amountInCents = operation.amountInCents,
                        type = operation.type.name,
                        recurrence = operation.recurrence.name,
                        startDate = operation.startDate,
                        endDate = operation.endDate,
                        updatedAt = now,
                        deletedAt = existing.deletedAt,
                        syncStatus = determineNextStatus(existing.syncStatus),
                        id = operation.id,
                        userId = uid
                    )
                }
            }
            Unit
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
            Unit
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
            onError = {
                AppLogger.get()?.e(tag, "Failed to sync ${operations.size} operations from server", it)
                CommonError.DatabaseError(it)
            }
        ) {
            queries.transaction {
                val uid = currentUserId
                operations.forEach { remote ->
                    val exists = queries.getOperationById(id = remote.id, userId = uid).executeAsOneOrNull() != null

                    if (exists) {
                        queries.updateOperation(
                            walletId = remote.walletId,
                            name = remote.name,
                            amountInCents = remote.amountInCents,
                            type = remote.type.name,
                            recurrence = remote.recurrence.name,
                            startDate = remote.startDate,
                            endDate = remote.endDate,
                            updatedAt = remote.updatedAt,
                            deletedAt = remote.deletedAt,
                            syncStatus = SyncStatus.SYNCED.name,
                            id = remote.id,
                            userId = uid
                        )
                    } else {
                        queries.insertOperation(
                            id = remote.id,
                            userId = uid,
                            walletId = remote.walletId,
                            name = remote.name,
                            amountInCents = remote.amountInCents,
                            type = remote.type.name,
                            recurrence = remote.recurrence.name,
                            startDate = remote.startDate,
                            endDate = remote.endDate,
                            createdAt = remote.createdAt,
                            updatedAt = remote.updatedAt,
                            deletedAt = remote.deletedAt,
                            syncStatus = SyncStatus.SYNCED.name
                        )
                    }
                }
            }
            Unit
        }
    }

    override suspend fun deleteAll(): Result<Unit, AppException> = withContext(dispatchers.io) {
        asResult(
            onError = {
                AppLogger.get()?.e(tag, "Failed to delete all operations", it)
                CommonError.DatabaseError(it)
            }
        ) {
            queries.deleteAllForUser(userId = currentUserId)
            Unit
        }
    }

    // ==========================================
    // 🛠️ PRIVATE HELPERS (Refactoring)
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