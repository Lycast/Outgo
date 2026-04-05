package fr.abknative.outgo.auth.api.usecase

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result

/**
 * Orchestrates the multi-layered account deletion process.
 */
interface DeleteAccountUseCase {
    /**
     * Executes the deletion based on user preferences.
     *
     * @param wipeLocal If true, clears the local SQLite database and preferences.
     * @param wipeServer If true, calls the Ktor API to delete/anonymize remote data.
     * @param revokeAuth If true, deletes the Firebase Authentication profile.
     * *Note: Setting this to true forces wipeServer to true.*
     * @return Result of the operation.
     */
    suspend operator fun invoke(
        wipeLocal: Boolean,
        wipeServer: Boolean,
        revokeAuth: Boolean
    ): Result<Unit, AppException>
}