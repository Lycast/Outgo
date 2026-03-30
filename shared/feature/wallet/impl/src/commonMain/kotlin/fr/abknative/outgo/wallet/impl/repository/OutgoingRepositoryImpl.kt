package fr.abknative.outgo.wallet.impl.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import fr.abknative.outgo.core.api.AppDispatchers
import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.core.api.logs.*
import fr.abknative.outgo.database.OutgoDatabase
import fr.abknative.outgo.wallet.api.model.Operation
import fr.abknative.outgo.wallet.api.repository.OutgoingRepository
import fr.abknative.outgo.wallet.impl.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * SQLDelight implementation of [OutgoingRepository].
 *
 * Uses [OutgoDatabase] as the local source of truth and handles
 * data mapping from database entities to domain models.
 */
internal class OutgoingRepositoryImpl(
    private val database: OutgoDatabase,
    private val dispatchers: AppDispatchers,
    private val timeProvider: TimeProvider
) : OutgoingRepository {

    private val queries = database.outgoingQueries
    private val tag = "OutgoingLocalRepo"

    override fun observeOutgoingsByMonth(month: Int): Flow<List<Operation>> {
        return queries.getOutgoingsByMonth(currentMonth = month.toLong())
            .asFlow()
            .mapToList(dispatchers.io)
            .map { entities -> entities.map { it.toDomain() } }
            .distinctUntilChanged()
    }

    override suspend fun insert(operation: Operation): Result<Unit, AppException> = asResult(
        onError = {
            AppLogger.get()?.e(tag, "Failed to insert outgoing: ${operation.id}", it)
            CommonError.DatabaseError(it)
        }
    ) {
        queries.transaction {
            queries.insertOutgoing(
                id = operation.id,
                budgetId = operation.budgetId,
                name = operation.name,
                amountInCents = operation.amountInCents,
                recurrence = operation.recurrence.name,
                dueDay = operation.dueDay.toLong(),
                dueMonth = operation.dueMonth?.toLong(),
                createdAt = operation.createdAt,
                updatedAt = operation.updatedAt,
                isDeleted = if (operation.isDeleted) 1L else 0L,
                syncStatus = operation.syncStatus.name
            )
        }
    }

    override suspend fun update(operation: Operation): Result<Unit, AppException> = asResult(
        onError = {
            AppLogger.get()?.e(tag, "Failed to update outgoing: ${operation.id}", it)
            CommonError.DatabaseError(it)
        }
    ) {
        queries.transaction {
            queries.updateOutgoing(
                name = operation.name,
                amountInCents = operation.amountInCents,
                recurrence = operation.recurrence.name,
                dueDay = operation.dueDay.toLong(),
                dueMonth = operation.dueMonth?.toLong(),
                updatedAt = operation.updatedAt,
                isDeleted = if (operation.isDeleted) 1L else 0L,
                syncStatus = operation.syncStatus.name,
                id = operation.id
            )
        }
    }

    override suspend fun markAsDeleted(id: String): Result<Unit, AppException> = asResult(
        onError = {
            AppLogger.get()?.e(tag, "Failed to mark outgoing as deleted: $id", it)
            CommonError.DatabaseError(it)
        }
    ) {
        queries.transaction {
            val current = queries.getById(id).executeAsOneOrNull()

            if (current?.syncStatus == SyncStatus.PENDING_CREATE.name) {
                queries.deletePhysical(id)
            } else {
                queries.markAsDeleted(
                    updatedAt = timeProvider.now(),
                    id = id
                )
            }
        }
    }

    override suspend fun getPendingOutgoings(): Result<List<Operation>, AppException> = asResult(
        onError = {
            AppLogger.get()?.e(tag, "Failed to fetch pending outgoings", it)
            CommonError.DatabaseError(it)
        }
    ) {
        queries.getPendingOutgoings().executeAsList().map { it.toDomain() }
    }

    override suspend fun updateSyncStatus(
        id: String,
        status: SyncStatus
    ): Result<Unit, AppException> = asResult(
        onError = {
            AppLogger.get()?.e(tag, "Failed to update sync status ($status) for outgoing: $id", it)
            CommonError.DatabaseError(it)
        }
    ) {
        queries.updateSyncStatus(syncStatus = status.name, id = id)
    }

    override suspend fun syncFromServer(operations: List<Operation>): Result<Unit, AppException> = asResult(
        onError = {
            AppLogger.get()?.e(tag, "Failed to sync ${operations.size} outgoings from server", it)
            CommonError.DatabaseError(it)
        }
    ) {
        queries.transaction {
            operations.forEach { remote ->
                val exists = queries.getById(remote.id).executeAsOneOrNull() != null

                if (exists) {
                    queries.updateOutgoing(
                        name = remote.name,
                        amountInCents = remote.amountInCents,
                        recurrence = remote.recurrence.name,
                        dueDay = remote.dueDay.toLong(),
                        dueMonth = remote.dueMonth?.toLong(),
                        updatedAt = remote.updatedAt,
                        isDeleted = if (remote.isDeleted) 1L else 0L,
                        syncStatus = SyncStatus.SYNCED.name,
                        id = remote.id
                    )
                } else {
                    queries.insertOutgoing(
                        id = remote.id,
                        budgetId = remote.budgetId,
                        name = remote.name,
                        amountInCents = remote.amountInCents,
                        recurrence = remote.recurrence.name,
                        dueDay = remote.dueDay.toLong(),
                        dueMonth = remote.dueMonth?.toLong(),
                        createdAt = remote.createdAt,
                        updatedAt = remote.updatedAt,
                        isDeleted = if (remote.isDeleted) 1L else 0L,
                        syncStatus = SyncStatus.SYNCED.name
                    )
                }
            }
        }
    }

    override suspend fun getOutgoingById(id: String): Operation? {
        return queries.getById(id).executeAsOneOrNull()?.toDomain()
    }
}