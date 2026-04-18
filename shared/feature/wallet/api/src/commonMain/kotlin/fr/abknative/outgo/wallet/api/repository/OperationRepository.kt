package fr.abknative.outgo.wallet.api.repository

import fr.abknative.outgo.core.api.EpochMillis
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.core.api.model.SyncStatus
import fr.abknative.outgo.wallet.api.model.Operation
import kotlinx.coroutines.flow.Flow

/**
 * Single source of truth for managing financial operations (Incomes and Expenses).
 *
 * This repository abstracts the underlying data sources, providing a unified
 * interface based on the absolute temporal engine.
 */
interface OperationRepository {

    /**
     * Provides a reactive stream of all active operations that overlap with the specified time period.
     * The flow emits a new list whenever the underlying data changes, excluding records marked as deleted.
     *
     * @param from The start of the temporal bounding box (EpochMillis).
     * @param to The end of the temporal bounding box (EpochMillis).
     * @return A [Flow] emitting the list of active [Operation]s falling within the timeframe.
     */
    fun observeOperationsByPeriod(walletId: String, from: EpochMillis, to: EpochMillis): Flow<List<Operation>>

    /**
     * Provides a reactive stream of all active operations (rules) for a specific wallet,
     * regardless of their start or end dates. Excludes records marked as deleted.
     * Used primarily for the "Standard/Global" view of the user's financial rules.
     *
     * @param walletId The unique identifier of the wallet.
     * @return A [Flow] emitting the complete list of active [Operation]s.
     */
    fun observeAllOperations(walletId: String): Flow<List<Operation>>

    /**
     * Observes all operations that have local modifications waiting to be pushed to the remote server.
     * Returns a continuous flow that emits a new list whenever a pending operation is created, updated, or successfully synced.
     *
     * @return A [Flow] emitting the list of pending Operations for the currently authenticated user.
     */
    fun observePendingOperations(): Flow<List<Operation>>

    /**
     * Retrieves a specific operation by its unique identifier.
     * Fetches the operation even if it is soft-deleted.
     *
     * @param id The unique identifier of the operation.
     * @return The [Operation] if found, or null otherwise.
     */
    suspend fun getOperationById(id: String): Operation?

    /**
     * Saves an operation to the local database.
     * Handles both insertion of new operations and updating of existing ones.
     *
     * @param operation The [Operation] to insert or update.
     * @return A [Result] indicating success or an [AppException] on failure.
     */
    suspend fun save(operation: Operation): Result<Unit, AppException>

    /**
     * Performs a logical (soft) deletion of an operation.
     * The record remains in local storage but is stamped with `deletedAt` for future
     * synchronization with the remote backend.
     *
     * @param id The unique identifier of the operation to delete.
     * @return A [Result] indicating success or an [AppException] on failure.
     */
    suspend fun markAsDeleted(id: String): Result<Unit, AppException>

    /**
     * Retrieves all operations that have local changes not yet synchronized with the remote server.
     * This includes items marked as PENDING_CREATE, PENDING_UPDATE, or PENDING_DELETE.
     * Used by the sync engine to collect data for the 'Push' operation.
     */
    suspend fun getPendingOperations(): Result<List<Operation>, AppException>

    /**
     * Updates the synchronization status of a specific operation identified by its [id].
     * Typically called to transition an item to [SyncStatus.SYNCED] after a successful server response.
     */
    suspend fun updateSyncStatus(id: String, status: SyncStatus): Result<Unit, AppException>

    /**
     * Synchronizes and merges a list of operations received from the remote server
     * into the local database.
     * Ensures the local state matches the server's source of truth during the 'Pull' operation.
     */
    suspend fun syncFromServer(operations: List<Operation>): Result<Unit, AppException>

    /**
    * Deletes operations based on the provided scope.
    * @param userId If provided, only deletes data for this specific user.
    * If null, performs a complete factory reset.
    */
    suspend fun deleteAll(userId: String?): Result<Unit, AppException>
}