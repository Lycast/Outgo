package fr.abknative.outgo.wallet.impl.migration

import fr.abknative.outgo.auth.api.AuthError
import fr.abknative.outgo.core.api.AppDispatchers
import fr.abknative.outgo.core.api.LocalDataMigrator
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.CommonError
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.database.OutgoDatabase
import kotlinx.coroutines.withContext

/**
 * Implementation of [LocalDataMigrator] using SQLDelight.
 * * Safely handles the migration of offline user data to an authenticated Firebase account.
 * Heavy database transactions are offloaded to the IO dispatcher to prevent UI thread blocking.
 */
internal class LocalDataMigratorImpl(
    private val database: OutgoDatabase,
    private val dispatchers: AppDispatchers
) : LocalDataMigrator {

    override suspend fun checkConflictAndMigrate(newUserId: String, currentLocalId: String): Result<Unit, AppException> {

        if (currentLocalId == newUserId) {
            return Result.Success(Unit)
        }

        if (currentLocalId.startsWith("local_")) {
            return withContext(dispatchers.io) {
                try {
                    database.transaction {
                        database.walletQueries.updateUserId(newUserId = newUserId, oldUserId = currentLocalId)
                        database.operationQueries.updateUserId(newUserId = newUserId, oldUserId = currentLocalId)
                    }
                    Result.Success(Unit)
                } catch (e: Exception) {
                    Result.Error(CommonError.DatabaseError(e))
                }
            }
        }

        return Result.Error(AuthError.DataConflict())
    }

    override suspend fun mergeLocalDataToAccount(newUserId: String, localId: String): Result<Unit, AppException> {
        return withContext(dispatchers.io) {
            try {
                database.transaction {
                    // On bascule la propriété des Wallets ET des Opérations vers le Firebase_UID
                    database.walletQueries.updateUserId(newUserId = newUserId, oldUserId = localId)
                    database.operationQueries.updateUserId(newUserId = newUserId, oldUserId = localId)
                }
                Result.Success(Unit)
            } catch (e: Exception) {
                Result.Error(CommonError.DatabaseError(e))
            }
        }
    }

    override suspend fun discardLocalData(localId: String): Result<Unit, AppException> {
        return withContext(dispatchers.io) {
            try {
                database.transaction {
                    // On purge complètement l'ID local
                    // Attention à l'ordre des suppressions si tu as des contraintes de clés étrangères (Foreign Keys)
                    database.operationQueries.deleteAllForUser(userId = localId)
                    database.walletQueries.deleteAllForUser(userId = localId)
                }
                Result.Success(Unit)
            } catch (e: Exception) {
                Result.Error(CommonError.DatabaseError(e))
            }
        }
    }
}