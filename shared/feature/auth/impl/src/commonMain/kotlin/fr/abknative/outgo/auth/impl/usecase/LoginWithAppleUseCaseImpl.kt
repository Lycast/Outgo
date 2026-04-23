package fr.abknative.outgo.auth.impl.usecase

import fr.abknative.outgo.auth.api.provider.SessionProvider
import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.auth.api.usecase.LoginWithAppleUseCase
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.sync.api.SyncManager

internal class LoginWithAppleUseCaseImpl(
    private val authRepository: AuthRepository,
    private val sessionProvider: SessionProvider,
    private val syncManager: SyncManager
) : LoginWithAppleUseCase {

    override suspend fun invoke(
        idToken: String
    ): Result<Unit, AppException> {
        return executeAuthWithMigration(
            sessionProvider,
            authRepository,
            syncManager
        ) {
            authRepository.loginWithApple(idToken)
        }
    }
}