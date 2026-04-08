package fr.abknative.outgo.auth.impl.usecase

import fr.abknative.outgo.auth.api.AuthError
import fr.abknative.outgo.auth.api.model.ConflictStrategy
import fr.abknative.outgo.auth.api.provider.SessionProvider
import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.core.api.LocalDataMigrator
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.sync.api.SyncManager

internal suspend inline fun executeAuthWithMigration(
    sessionProvider: SessionProvider,
    localDataMigrator: LocalDataMigrator,
    authRepository: AuthRepository,
    syncManager: SyncManager,
    conflictStrategy: ConflictStrategy? = null,
    crossinline authAction: suspend () -> Result<Unit, AppException>
): Result<Unit, AppException> {

    val currentLocalId = sessionProvider.getCurrentUserId()

    val authResult = authAction()
    if (authResult is Result.Error) return authResult

    val newUserId = sessionProvider.getCurrentUserId()

    if (currentLocalId == newUserId) {
        return Result.Success(Unit)
    }

    if (currentLocalId.startsWith("local_")) {

        if (conflictStrategy != null) {
            val resolutionResult = when (conflictStrategy) {
                ConflictStrategy.MERGE -> localDataMigrator.mergeLocalDataToAccount(newUserId, currentLocalId)
                ConflictStrategy.DISCARD_LOCAL -> localDataMigrator.discardLocalData(currentLocalId)
            }
            if (resolutionResult is Result.Error) {
                authRepository.logout()
                return resolutionResult
            }
            return Result.Success(Unit)
        }

        val hasRemoteResult = syncManager.hasRemoteData()

        if (hasRemoteResult is Result.Error) {
            authRepository.logout()
            return hasRemoteResult
        }

        val hasRemoteData = (hasRemoteResult as Result.Success).data

        if (!hasRemoteData) {
            val migrationResult = localDataMigrator.checkConflictAndMigrate(newUserId, currentLocalId)
            if (migrationResult is Result.Error) {
                authRepository.logout()
                return migrationResult
            }
        } else {
            authRepository.logout()
            return Result.Error(AuthError.DataConflict())
        }
    } else {
        syncManager.clearSyncState()
    }

    return Result.Success(Unit)
}