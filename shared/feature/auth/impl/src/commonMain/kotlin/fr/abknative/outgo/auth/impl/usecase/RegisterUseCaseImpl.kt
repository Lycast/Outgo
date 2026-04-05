package fr.abknative.outgo.auth.impl.usecase

import fr.abknative.outgo.auth.api.provider.SessionProvider
import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.auth.api.usecase.RegisterUseCase
import fr.abknative.outgo.core.api.LocalDataMigrator
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result

internal class RegisterUseCaseImpl(
    private val authRepository: AuthRepository,
    private val sessionProvider: SessionProvider,
    private val localDataMigrator: LocalDataMigrator
) : RegisterUseCase {

    override suspend fun invoke(email: String, password: String): Result<Unit, AppException> {
        return executeAuthWithMigration(sessionProvider, localDataMigrator, authRepository) {
            authRepository.register(email, password)
        }
    }
}