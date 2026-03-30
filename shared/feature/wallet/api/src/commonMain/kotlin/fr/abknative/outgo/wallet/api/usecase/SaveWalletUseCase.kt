package fr.abknative.outgo.wallet.api.usecase

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result

/**
 * Validates and saves a financial wallet (e.g., a bank account or cash envelope).
 * Acts as an "upsert" operation: it creates a new wallet if no ID is provided,
 * or updates the existing one.
 */
interface SaveWalletUseCase {
    /**
     * Executes the save operation.
     *
     * @param id The unique identifier (UUID) of the wallet. If null, a new UUID is generated.
     * @param name The display name of the wallet. Must not be blank.
     * @return A [Result] indicating success or containing an [AppException] on validation failure.
     */
    suspend operator fun invoke(
        id: String? = null,
        name: String
    ): Result<Unit, AppException>
}