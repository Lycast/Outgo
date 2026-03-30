package fr.abknative.outgo.wallet.api.usecase

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result

/**
 * Handles the deletion of a specific wallet.
 * Performs a logical (soft) deletion to ensure data consistency during offline-first
 * synchronization, rather than a hard removal from the local database.
 */
interface DeleteWalletUseCase {
    /**
     * Executes the soft-delete operation for a wallet.
     * Note: Deleting a wallet should cascade and soft-delete all its associated operations.
     *
     * @param id The unique identifier of the wallet to delete.
     * @return A [Result] indicating success or containing an [AppException] on failure.
     */
    suspend operator fun invoke(id: String): Result<Unit, AppException>
}