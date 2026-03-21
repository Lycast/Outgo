package fr.abknative.outgo.outgoing.api.repository

import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.core.api.Result
import fr.abknative.outgo.outgoing.api.model.Outgoing
import kotlinx.coroutines.flow.Flow

/**
 * Single source of truth for managing outgoing data.
 *
 * This repository abstracts the underlying data sources (local database or remote API),
 * providing a unified interface for the domain layer.
 */
interface OutgoingRepository {

    /**
     * Provides a reactive stream of all outgoings for a specific month.
     * The flow emits a new list whenever the underlying data changes,
     * excluding records marked as deleted.
     *
     * @param month The target month (e.g., 1 for January, 12 for December).
     * @return A [Flow] emitting the list of active [Outgoing] expenses.
     */
    fun observeOutgoingsByMonth(month: Int): Flow<List<Outgoing>>

    /**
     * Retrieves a specific outgoing expense by its unique identifier.
     *
     * @param id The unique identifier of the expense.
     * @return The [Outgoing] if found, or null otherwise.
     */
    suspend fun getOutgoingById(id: String): Outgoing?

    /**
     * Inserts a new outgoing expense. Fails if the ID already exists.
     *
     * @param outgoing The [Outgoing] to insert.
     * @return A [Result] indicating success or an [AppException] on failure.
     */
    suspend fun insert(outgoing: Outgoing): Result<Unit, AppException>

    /**
     * Updates an existing outgoing expense.
     *
     * @param outgoing The [Outgoing] containing updated values.
     * @return A [Result] indicating success or an [AppException] on failure.
     */
    suspend fun update(outgoing: Outgoing): Result<Unit, AppException>

    /**
     * Performs a logical (soft) deletion of an outgoing expense.
     * The record remains in local storage but is marked for future
     * synchronization with the remote backend.
     *
     * @param id The unique identifier of the expense to delete.
     * @return A [Result] indicating success or an [AppException] on failure.
     */
    suspend fun markAsDeleted(id: String): Result<Unit, AppException>
}