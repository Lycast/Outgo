package fr.abknative.outgo.auth.impl.usecase

import fr.abknative.outgo.auth.api.provider.SessionProvider
import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.auth.api.usecase.LoginUseCase
import fr.abknative.outgo.core.api.AppDispatchers
import fr.abknative.outgo.core.api.LocalDataMigrator
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.sync.api.SyncManager

internal class LoginUseCaseImpl(
    private val authRepository: AuthRepository,
    private val sessionProvider: SessionProvider,
    private val localDataMigrator: LocalDataMigrator,
    private val dispatchers: AppDispatchers,
    private val syncManager: SyncManager
) : LoginUseCase {

    override suspend fun invoke(
        email: String,
        password: String,
        forceSwitch: Boolean
    ): Result<Unit, AppException> {
        return executeAuthWithMigration(
            sessionProvider,
            localDataMigrator,
            authRepository,
            syncManager,
            forceSwitch,
            dispatchers
        ) {
            authRepository.login(email, password)
        }
    }
}