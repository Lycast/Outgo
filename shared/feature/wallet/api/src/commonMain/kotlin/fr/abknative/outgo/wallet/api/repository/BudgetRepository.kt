package fr.abknative.outgo.wallet.api.repository

import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.wallet.api.model.Wallet
import kotlinx.coroutines.flow.Flow

/**
 * Manages the global financial context, primarily the user's income and budget settings.
 */
interface BudgetRepository {

    /**
     * Emits the budget reactively.
     * @param id The budget identifier to observe.
     * @return A [Flow] that can emit null if the budget hasn't been created yet on first launch.
     */
    fun observeBudget(id: String = "default"): Flow<Wallet?>

    /**
     * Performs a one-shot retrieval of the current budget, typically used for initial existence checks.
     *
     * @param id The budget identifier to retrieve.
     * @return A [Result] containing the [Wallet] if found, or null.
     */
    suspend fun getBudget(id: String = "default"): Result<Wallet?, AppException>

    /**
     * Inserts a new budget into the database.
     *
     * @param wallet The [Wallet] to insert.
     * @return A [Result] indicating success or an [AppException] on failure.
     */
    suspend fun insert(wallet: Wallet): Result<Unit, AppException>

    /**
     * Updates an already existing budget.
     *
     * @param wallet The [Wallet] containing the updated values.
     * @return A [Result] indicating success or an [AppException] on failure.
     */
    suspend fun update(wallet: Wallet): Result<Unit, AppException>

    /**
     * Retrieves all budgets that have local changes not yet synchronized with the server.
     * Used by the sync engine to collect data for the 'Push' operation.
     */
    suspend fun getPendingBudgets(): Result<List<Wallet>, AppException>

    /**
     * Updates the synchronization status of a specific budget identified by its [id].
     * Typically called after a successful upload to the server.
     */
    suspend fun updateSyncStatus(id: String, status: SyncStatus): Result<Unit, AppException>

    /**
     * Integrates budget data received from the remote server into the local database.
     * Resolves state between local and remote data to maintain a consistent 'Pull' operation.
     */
    suspend fun syncFromServer(wallets: List<Wallet>): Result<Unit, AppException>
}