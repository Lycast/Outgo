package fr.abknative.outgo.auth.impl.usecase

import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.auth.api.usecase.LogoutUseCase
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.sync.api.SyncManager

internal class LogoutUseCaseImpl(
    private val repository: AuthRepository,
    private val syncManager: SyncManager
) : LogoutUseCase {

    override suspend fun invoke(): Result<Unit, AppException> {
        syncManager.clearSyncState()

        return repository.logout()
    }
}