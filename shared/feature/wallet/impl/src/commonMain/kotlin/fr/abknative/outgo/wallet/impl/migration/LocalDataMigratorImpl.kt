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
 * Implementation of [fr.abknative.outgo.core.api.LocalDataMigrator] using SQLDelight.
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
}