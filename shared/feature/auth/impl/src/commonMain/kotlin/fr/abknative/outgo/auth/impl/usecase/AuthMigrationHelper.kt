package fr.abknative.outgo.auth.impl.usecase

import fr.abknative.outgo.auth.api.provider.SessionProvider
import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.core.api.AppDispatchers
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.AppLogger
import fr.abknative.outgo.core.api.logs.CommonError
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.sync.api.SyncManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal suspend inline fun executeAuthWithMigration(
    sessionProvider: SessionProvider,
    authRepository: AuthRepository,
    syncManager: SyncManager,
    dispatchers: AppDispatchers,
    crossinline authAction: suspend () -> Result<Unit, AppException>
): Result<Unit, AppException> {

    val tag = "AuthMigration"
    val currentLocalId = sessionProvider.getCurrentUserId()
    AppLogger.get()?.d(tag, "before auth action - ID : $currentLocalId")

    val authResult = authAction()
    if (authResult is Result.Error) return authResult

    val currentSession = authRepository.observeSession().value
        ?: return Result.Error(CommonError.UnknownError(IllegalStateException("Session lost after successful auth")))

    val newUserId = currentSession.userId
    AppLogger.get()?.d(tag, "after auth action - New ID Cloud : $newUserId")

    if (currentLocalId == newUserId) {
        sessionProvider.commitPersistentId(newUserId)
        AppLogger.get()?.d(tag, "Same ID. Commit : $newUserId")
    } else {
        if (!currentLocalId.startsWith("local_")) {
            syncManager.clearSyncState()
        }

        sessionProvider.commitPersistentId(newUserId)
        AppLogger.get()?.d(tag, "ID Changed. Commit : $newUserId")
    }

    CoroutineScope(dispatchers.io).launch {
        AppLogger.get()?.d(tag, "Triggering background SyncIn for new ID: ${sessionProvider.getCurrentUserId()}")
        syncManager.syncIn()
    }

    return Result.Success(Unit)
}