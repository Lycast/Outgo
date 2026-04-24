package fr.abknative.outgo.auth.api.usecase

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result

/**
 * Wipes remote server data, downgrades local data to a local identity,
 * and logs the user out of the current session without deleting the remote authentication account.
 */
interface WipeDataAndLogoutUseCase {
    suspend operator fun invoke(): Result<Unit, AppException>
}