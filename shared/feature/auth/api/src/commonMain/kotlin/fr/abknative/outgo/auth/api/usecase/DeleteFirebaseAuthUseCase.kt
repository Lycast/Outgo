package fr.abknative.outgo.auth.api.usecase

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result

/**
 * Permanently deletes the user's authentication account from the identity provider.
 * This operation is sensitive and requires a recently authenticated session.
 */
interface DeleteFirebaseAuthUseCase {
    suspend operator fun invoke(): Result<Unit, AppException>
}