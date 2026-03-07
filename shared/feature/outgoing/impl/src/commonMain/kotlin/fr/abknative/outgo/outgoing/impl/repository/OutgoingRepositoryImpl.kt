package fr.abknative.outgo.outgoing.impl.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import fr.abknative.outgo.core.api.AppDispatchers
import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.core.api.CommonError
import fr.abknative.outgo.core.api.Result
import fr.abknative.outgo.core.api.asResult
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.database.OutgoDatabase
import fr.abknative.outgo.outgoing.api.model.Outgoing
import fr.abknative.outgo.outgoing.api.repository.OutgoingRepository
import fr.abknative.outgo.outgoing.impl.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * SQLDelight implementation of [OutgoingRepository].
 *
 * Uses [OutgoDatabase] as the local source of truth and handles
 * data mapping from database entities to domain models.
 */
internal class OutgoingRepositoryImpl(
    database: OutgoDatabase,
    private val dispatchers: AppDispatchers,
    private val timeProvider: TimeProvider
) : OutgoingRepository {

    private val queries = database.outgoingQueries

    override fun observeActiveOutgoings(): Flow<List<Outgoing>> {
        return queries.selectAllActive()
            .asFlow()
            .mapToList(dispatchers.io)
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun upsert(outgoing: Outgoing): Result<Unit, AppException> = asResult(
        onError = { CommonError.DatabaseError(it) }
    ) {
        queries.upsert(
            id = outgoing.id,
            name = outgoing.name,
            amountInCents = outgoing.amountInCents,
            cycle = outgoing.cycle.name,
            billingDay = outgoing.billingDay.toLong(),
            createdAt = outgoing.createdAt,
            updatedAt = outgoing.updatedAt,
            isDeleted = if (outgoing.isDeleted) 1L else 0L,
            syncStatus = outgoing.syncStatus.name
        )
    }

    override suspend fun markAsDeleted(id: String): Result<Unit, AppException> = asResult(
        onError = { CommonError.DatabaseError(it) }
    ) {
        queries.markAsDeleted(
            updatedAt = timeProvider.now(),
            id = id
        )
    }

    override suspend fun getPendingSyncItems(): List<Outgoing> {
        return queries.selectAllPendingSync()
            .executeAsList()
            .map { it.toDomain() }
    }
}