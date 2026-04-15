package fr.abknative.outgo.auth.impl.usecase

import fr.abknative.outgo.auth.api.AuthError
import fr.abknative.outgo.auth.api.provider.SessionProvider
import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.core.api.AppDispatchers
import fr.abknative.outgo.core.api.LocalDataMigrator
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.AppLogger
import fr.abknative.outgo.core.api.logs.CommonError
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.sync.api.SyncManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal suspend inline fun executeAuthWithMigration(
    sessionProvider: SessionProvider,
    localDataMigrator: LocalDataMigrator,
    authRepository: AuthRepository,
    syncManager: SyncManager,
    forceSwitch: Boolean = false,
    dispatchers: AppDispatchers,
    crossinline authAction: suspend () -> Result<Unit, AppException>
): Result<Unit, AppException> {

    val tag = "AuthMigration"

    val currentLocalId = sessionProvider.getCurrentUserId()
    AppLogger.get()?.d(tag, "before login - ID : $currentLocalId")

    val authResult = authAction()
    if (authResult is Result.Error) return authResult

    val currentSession = authRepository.observeSession().value
        ?: return Result.Error(CommonError.UnknownError(IllegalStateException("Session lost")))

    val newUserId = currentSession.userId
    AppLogger.get()?.d(tag, "after login - New ID Cloud : $newUserId")

    // Fast-path: Same account
    if (currentLocalId == newUserId) {
        sessionProvider.commitPersistentId(newUserId)
        AppLogger.get()?.d(tag, "Migration ID commit : $newUserId")
        return Result.Success(Unit)
    }

    if (currentLocalId.startsWith("local_")) {
        val hasRemoteResult = syncManager.hasRemoteData()
        if (hasRemoteResult is Result.Error) {
            authRepository.logout()
            return hasRemoteResult
        }

        val serverHasData = (hasRemoteResult as Result.Success).data

        if (!serverHasData) {
            /** CASE A: New Cloud Account. Silent migration of local work. */
            localDataMigrator.checkConflictAndMigrate(newUserId, currentLocalId)
            sessionProvider.commitPersistentId(newUserId)
        } else {
            /** CASE B: Existing Cloud Account. Need user consent to switch view. */
            if (!forceSwitch) {
                authRepository.logout()
                AppLogger.get()?.d(tag, "conflict - ID blocked. keep id : $currentLocalId")
                return Result.Error(AuthError.DataConflict())
            }

            // Strategy received: User accepted to switch to Cloud view.
            sessionProvider.commitPersistentId(newUserId)
            AppLogger.get()?.d(tag, "Force switch - Conflit resolved. ID commit : $newUserId")
        }
    } else {
        /** CASE C: Switching from one Cloud account to another. */
        syncManager.clearSyncState()
        sessionProvider.commitPersistentId(newUserId)
        AppLogger.get()?.d(tag, "Loading CLOUD->CLOUD ID commit : $newUserId")
    }

    CoroutineScope(dispatchers.io).launch {
        AppLogger.get()?.d(tag, "Synchro ID used : ${sessionProvider.getCurrentUserId()}")
        syncManager.syncIn()
    }

    return Result.Success(Unit)
}