package fr.abknative.outgo.auth.impl.usecase

import fr.abknative.outgo.auth.api.AuthError
import fr.abknative.outgo.auth.api.provider.SessionProvider
import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.core.api.LocalDataMigrator
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result

/**
 * Encapsule la logique commune de migration et de vérification des conflits
 * après une tentative d'authentification (Login ou Register).
 */
internal suspend inline fun executeAuthWithMigration(
    sessionProvider: SessionProvider,
    localDataMigrator: LocalDataMigrator,
    authRepository: AuthRepository,
    crossinline authAction: suspend () -> Result<Unit, AppException>
): Result<Unit, AppException> {

    val currentLocalId = sessionProvider.getCurrentUserId()

    // On exécute l'action spécifique (login ou register)
    val authResult = authAction()

    if (authResult is Result.Error) {
        return authResult
    }

    val newUserId = sessionProvider.getCurrentUserId()
    val migrationResult = localDataMigrator.checkConflictAndMigrate(newUserId, currentLocalId)

    if (migrationResult is Result.Error) {
        authRepository.logout()
        return Result.Error(AuthError.DataConflict())
    }

    return Result.Success(Unit)
}