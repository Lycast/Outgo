package fr.abknative.outgo.wallet.impl.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import fr.abknative.outgo.core.api.AppDispatchers
import fr.abknative.outgo.core.api.EpochMillis
import fr.abknative.outgo.core.api.IdProvider
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.core.api.logs.*
import fr.abknative.outgo.core.api.model.SyncStatus
import fr.abknative.outgo.database.OutgoDatabase
import fr.abknative.outgo.wallet.api.model.Operation
import fr.abknative.outgo.wallet.api.repository.OperationRepository
import fr.abknative.outgo.wallet.impl.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * SQLDelight implementation of [OperationRepository].
 *
 * Uses [OutgoDatabase] as the local source of truth and handles
 * data mapping from database entities to domain models.
 */
internal class OperationRepositoryImpl(
    private val database: OutgoDatabase,
    private val dispatchers: AppDispatchers,
    private val timeProvider: TimeProvider,
    private val idProvider: IdProvider
) : OperationRepository {

    private val queries = database.operationQueries
    private val tag = "OperationLocalRepo"

    override fun observeOperationsByPeriod(walletId: String, from: EpochMillis, to: EpochMillis): Flow<List<Operation>> {
        return queries.getOperationsByPeriod(walletId = walletId, from = from, to = to)
            .asFlow()
            .mapToList(dispatchers.io)
            .map { entities -> entities.map { it.toDomain() } }
            .distinctUntilChanged()
    }

    override suspend fun getOperationById(id: String): Operation? {
        return queries.getOperationById(id).executeAsOneOrNull()?.toDomain()
    }

    override suspend fun save(operation: Operation): Result<Unit, AppException> = asResult(
        onError = {
            AppLogger.get()?.e(tag, "Failed to save operation: ${operation.id}", it)
            CommonError.DatabaseError(it)
        }
    ) {
        queries.transaction {
            val now = timeProvider.now()
            val existing = queries.getOperationById(operation.id).executeAsOneOrNull()

            if (existing == null) {
                val finalId = operation.id.ifBlank { idProvider.generate() }
                queries.insertOperation(
                    id = finalId,
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
            } else {
                val currentStatus = SyncStatus.fromString(existing.syncStatus)
                val nextStatus = if (currentStatus == SyncStatus.PENDING_CREATE) {
                    SyncStatus.PENDING_CREATE
                } else {
                    SyncStatus.PENDING_UPDATE
                }
                val hasChanged = existing.name != operation.name ||
                        existing.amountInCents != operation.amountInCents ||
                        existing.startDate != operation.startDate ||
                        existing.endDate != operation.endDate ||
                        existing.type != operation.type.name ||
                        existing.recurrence != operation.recurrence.name

                if (hasChanged) {
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
                        syncStatus = nextStatus.name,
                        id = operation.id
                    )
                }
            }
        }
    }

    override suspend fun markAsDeleted(id: String): Result<Unit, AppException> = asResult(
        onError = {
            AppLogger.get()?.e(tag, "Failed to mark operation as deleted: $id", it)
            CommonError.DatabaseError(it)
        }
    ) {
        queries.transaction {
            val current = queries.getOperationById(id).executeAsOneOrNull()

            if (current?.syncStatus == SyncStatus.PENDING_CREATE.name) {
                queries.hardDeletePendingCreate(id)
            } else {
                queries.markAsDeleted(
                    deletedAt = timeProvider.now(),
                    updatedAt = timeProvider.now(),
                    id = id
                )
            }
        }
    }

    override suspend fun getPendingOperations(): Result<List<Operation>, AppException> = asResult(
        onError = {
            AppLogger.get()?.e(tag, "Failed to fetch pending operations", it)
            CommonError.DatabaseError(it)
        }
    ) {
        queries.getPendingOperations().executeAsList().map { it.toDomain() }
    }

    override suspend fun updateSyncStatus(
        id: String,
        status: SyncStatus
    ): Result<Unit, AppException> = asResult(
        onError = {
            AppLogger.get()?.e(tag, "Failed to update sync status ($status) for operation: $id", it)
            CommonError.DatabaseError(it)
        }
    ) {
        queries.updateSyncStatus(syncStatus = status.name, id = id)
    }

    override suspend fun syncFromServer(operations: List<Operation>): Result<Unit, AppException> = asResult(
        onError = {
            AppLogger.get()?.e(tag, "Failed to sync ${operations.size} operations from server", it)
            CommonError.DatabaseError(it)
        }
    ) {
        queries.transaction {
            operations.forEach { remote ->
                val exists = queries.getOperationById(remote.id).executeAsOneOrNull() != null

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
                        id = remote.id
                    )
                } else {
                    queries.insertOperation(
                        id = remote.id,
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
    }

    override suspend fun deleteAll(): Result<Unit, AppException> = asResult(
        onError = {
            AppLogger.get()?.e(tag, "Failed to delete all operations", it)
            CommonError.DatabaseError(it)
        }
    ) {
        queries.deleteAll()
    }
}