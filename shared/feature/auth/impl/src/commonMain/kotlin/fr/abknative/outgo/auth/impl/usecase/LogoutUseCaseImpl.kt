package fr.abknative.outgo.auth.impl.usecase

import fr.abknative.outgo.auth.api.provider.SessionProvider
import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.auth.api.usecase.LogoutUseCase
import fr.abknative.outgo.auth.impl.provider.SessionProviderImpl
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.sync.api.SyncManager

internal class LogoutUseCaseImpl(
    private val repository: AuthRepository,
    private val sessionProvider: SessionProvider,
    private val syncManager: SyncManager
) : LogoutUseCase {

    override suspend fun invoke(displayLocalData: Boolean): Result<Unit, AppException> {

        if (!displayLocalData) { (sessionProvider as? SessionProviderImpl)?.revertToLastLocalId() }

        syncManager.clearSyncState()
        return repository.logout()
    }
}