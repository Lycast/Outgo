package fr.abknative.outgo.wallet.api.repository

import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.wallet.api.model.Operation
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
     * @return A [Flow] emitting the list of active [Operation] expenses.
     */
    fun observeOutgoingsByMonth(month: Int): Flow<List<Operation>>

    /**
     * Retrieves a specific outgoing expense by its unique identifier.
     *
     * @param id The unique identifier of the expense.
     * @return The [Operation] if found, or null otherwise.
     */
    suspend fun getOutgoingById(id: String): Operation?

    /**
     * Inserts a new outgoing expense. Fails if the ID already exists.
     *
     * @param operation The [Operation] to insert.
     * @return A [Result] indicating success or an [AppException] on failure.
     */
    suspend fun insert(operation: Operation): Result<Unit, AppException>

    /**
     * Updates an existing outgoing expense.
     *
     * @param operation The [Operation] containing updated values.
     * @return A [Result] indicating success or an [AppException] on failure.
     */
    suspend fun update(operation: Operation): Result<Unit, AppException>

    /**
     * Performs a logical (soft) deletion of an outgoing expense.
     * The record remains in local storage but is marked for future
     * synchronization with the remote backend.
     *
     * @param id The unique identifier of the expense to delete.
     * @return A [Result] indicating success or an [AppException] on failure.
     */
    suspend fun markAsDeleted(id: String): Result<Unit, AppException>

    /**
     * Retrieves all outgoing expenses that have local changes not yet synchronized with the remote server.
     * This includes items marked as PENDING_CREATE, PENDING_UPDATE, or PENDING_DELETE.
     * Used by the sync engine to collect data for the 'Push' operation.
     */
    suspend fun getPendingOutgoings(): Result<List<Operation>, AppException>

    /**
     * Updates the synchronization status of a specific outgoing expense identified by its [id].
     * Typically called to transition an item to [SyncStatus.SYNCED] after a successful server response.
     */
    suspend fun updateSyncStatus(id: String, status: SyncStatus): Result<Unit, AppException>

    /**
     * Synchronizes and merges a list of outgoing expenses received from the remote server
     * into the local database.
     * Handles conflict resolution and ensures the local state matches the server's
     * source of truth during the 'Pull' operation.
     */
    suspend fun syncFromServer(operations: List<Operation>): Result<Unit, AppException>
}