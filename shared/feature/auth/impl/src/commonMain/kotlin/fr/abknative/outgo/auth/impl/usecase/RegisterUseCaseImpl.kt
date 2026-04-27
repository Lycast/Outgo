package fr.abknative.outgo.auth.impl.usecase

import fr.abknative.outgo.auth.api.provider.SessionProvider
import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.auth.api.usecase.RegisterUseCase
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.sync.api.SyncManager

internal class RegisterUseCaseImpl(
    private val authRepository: AuthRepository,
    private val sessionProvider: SessionProvider,
    private val syncManager: SyncManager
) : RegisterUseCase {

    override suspend fun invoke(
        email: String,
        password: String
    ): Result<Unit, AppException> {
        return executeAuthWithMigration(
            sessionProvider,
            authRepository,
            syncManager
        ) {
            val registerResult = authRepository.register(email, password)
            if (registerResult is Result.Error) return@executeAuthWithMigration registerResult

            authRepository.sendEmailVerification()
        }
    }
}