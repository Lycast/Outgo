package fr.abknative.outgo.outgoing.impl.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import fr.abknative.outgo.core.api.*
import fr.abknative.outgo.database.OutgoDatabase
import fr.abknative.outgo.outgoing.api.model.Outgoing
import fr.abknative.outgo.outgoing.api.repository.OutgoingRepository
import fr.abknative.outgo.outgoing.impl.mapper.toDomain
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

    override fun observeOutgoingsByMonth(month: Int): Flow<List<Outgoing>> {
        return queries.getOutgoingsByMonth(currentMonth = month.toLong())
            .asFlow()
            .mapToList(dispatchers.io)
            .map { entities -> entities.map { it.toDomain() } }
            .distinctUntilChanged()
    }

    override suspend fun insert(outgoing: Outgoing): Result<Unit, AppException> = asResult(
        onError = { CommonError.DatabaseError(it) }
    ) {
        queries.transaction {
            queries.insertOutgoing(
                id = outgoing.id,
                budgetId = outgoing.budgetId,
                name = outgoing.name,
                amountInCents = outgoing.amountInCents,
                recurrence = outgoing.recurrence.name,
                dueDay = outgoing.dueDay.toLong(),
                dueMonth = outgoing.dueMonth?.toLong(),
                createdAt = outgoing.createdAt,
                updatedAt = outgoing.updatedAt,
                isDeleted = if (outgoing.isDeleted) 1L else 0L,
                syncStatus = outgoing.syncStatus.name
            )
        }
    }

    override suspend fun update(outgoing: Outgoing): Result<Unit, AppException> = asResult(
        onError = { CommonError.DatabaseError(it) }
    ) {
        queries.transaction {
            queries.updateOutgoing(
                name = outgoing.name,
                amountInCents = outgoing.amountInCents,
                recurrence = outgoing.recurrence.name,
                dueDay = outgoing.dueDay.toLong(),
                dueMonth = outgoing.dueMonth?.toLong(),
                updatedAt = outgoing.updatedAt,
                isDeleted = if (outgoing.isDeleted) 1L else 0L,
                syncStatus = outgoing.syncStatus.name,
                id = outgoing.id
            )
        }
    }

    override suspend fun markAsDeleted(id: String): Result<Unit, AppException> = asResult(
        onError = { CommonError.DatabaseError(it) }
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

    override suspend fun getPendingOutgoings(): Result<List<Outgoing>, AppException> = asResult(
        onError = { CommonError.DatabaseError(it) }
    ) {
        queries.getPendingOutgoings().executeAsList().map { it.toDomain() }
    }

    override suspend fun updateSyncStatus(
        id: String,
        status: SyncStatus
    ): Result<Unit, AppException> = asResult(
        onError = { CommonError.DatabaseError(it) }
    ) {
        queries.updateSyncStatus(syncStatus = status.name, id = id)
    }

    override suspend fun syncFromServer(outgoings: List<Outgoing>): Result<Unit, AppException> = asResult(
        onError = { CommonError.DatabaseError(it) }
    ) {
        queries.transaction {
            outgoings.forEach { remote ->
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
                        syncStatus = SyncStatus.SYNCED.name, // Toujours SYNCED car vient du serveur
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

    override suspend fun getOutgoingById(id: String): Outgoing? {
        return queries.getById(id).executeAsOneOrNull()?.toDomain()
    }
}