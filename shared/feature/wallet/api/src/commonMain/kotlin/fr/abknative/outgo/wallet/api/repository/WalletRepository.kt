package fr.abknative.outgo.wallet.api.repository

import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.wallet.api.model.Wallet
import kotlinx.coroutines.flow.Flow

/**
 * Manages the global financial containers (Wallets).
 * * This repository abstracts the underlying data sources (local database or remote API),
 * providing a unified interface for the domain layer to manage accounts or physical envelopes.
 */
interface WalletRepository {

    /**
     * Provides a reactive stream of all active wallets.
     * The flow emits a new list whenever the underlying data changes,
     * strictly excluding records marked as deleted.
     *
     * @return A [Flow] emitting the list of active [Wallet]s.
     */
    fun observeActiveWallets(): Flow<List<Wallet>>

    /**
     * Performs a one-shot retrieval of a specific wallet by its unique identifier.
     * This query fetches the wallet even if it is marked as deleted (useful for sync resolution).
     *
     * @param id The unique identifier of the wallet.
     * @return A [Result] containing the [Wallet] if found, or null.
     */
    suspend fun getWalletById(id: String): Result<Wallet?, AppException>

    /**
     * Saves a wallet to the local database.
     * Handles both insertion of new wallets and updating of existing ones intelligently.
     *
     * @param wallet The [Wallet] to insert or update.
     * @return A [Result] indicating success or an [AppException] on failure.
     */
    suspend fun save(wallet: Wallet): Result<Unit, AppException>

    /**
     * Performs a logical (soft) deletion of a wallet.
     * The record remains in local storage but is marked with a `deletedAt` timestamp
     * for future synchronization with the remote backend.
     *
     * @param id The unique identifier of the wallet to delete.
     * @return A [Result] indicating success or an [AppException] on failure.
     */
    suspend fun markAsDeleted(id: String): Result<Unit, AppException>

    /**
     * Retrieves all wallets that have local changes not yet synchronized with the remote server.
     * This includes items marked as PENDING_CREATE, PENDING_UPDATE, or PENDING_DELETE.
     * Used by the sync engine to collect data for the 'Push' operation.
     */
    suspend fun getPendingWallets(): Result<List<Wallet>, AppException>

    /**
     * Updates the synchronization status of a specific wallet identified by its [id].
     * Typically called to transition an item to [SyncStatus.SYNCED] after a successful server response.
     */
    suspend fun updateSyncStatus(id: String, status: SyncStatus): Result<Unit, AppException>

    /**
     * Synchronizes and merges a list of wallets received from the remote server
     * into the local database.
     * Handles conflict resolution (Server-Authoritative) and ensures the local state
     * matches the server's source of truth during the 'Pull' operation.
     */
    suspend fun syncFromServer(wallets: List<Wallet>): Result<Unit, AppException>
}