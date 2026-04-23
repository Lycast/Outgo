package fr.abknative.outgo.auth.impl.usecase

import fr.abknative.outgo.auth.api.provider.SessionProvider
import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.auth.api.usecase.RegisterUseCase
import fr.abknative.outgo.core.api.AppDispatchers
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.sync.api.SyncManager

internal class RegisterUseCaseImpl(
    private val authRepository: AuthRepository,
    private val sessionProvider: SessionProvider,
    private val syncManager: SyncManager,
    private val dispatchers: AppDispatchers
) : RegisterUseCase {

    override suspend fun invoke(
        email: String,
        password: String
    ): Result<Unit, AppException> {
        return executeAuthWithMigration(
            sessionProvider,
            authRepository,
            syncManager,
            dispatchers
        ) {
            authRepository.register(email, password)
        }
    }
}