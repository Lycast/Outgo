package fr.abknative.outgo.auth.api.usecase

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result

/**
 * Orchestrates the login process.
 * * @param forceSwitch If true, bypasses the local data conflict warning and switches identity to the cloud account.
 */
interface LoginUseCase {
    suspend operator fun invoke(
        email: String,
        password: String,
        bypassMigration: Boolean = false
    ): Result<Unit, AppException>
}