package fr.abknative.outgo.wallet.impl

import fr.abknative.outgo.core.api.AppDispatchers
import fr.abknative.outgo.core.api.LocalDataDowngrader
import fr.abknative.outgo.database.OutgoDatabase
import kotlinx.coroutines.withContext

internal class WalletDataDowngrader(
    private val database: OutgoDatabase,
    private val dispatchers: AppDispatchers
) : LocalDataDowngrader {

    override suspend fun resetSyncStatusToPending(userId: String, now: Long) {
        withContext(dispatchers.io) {
            database.transaction {
                database.walletQueries.resetAllToPendingCreate(now = now, userId = userId)
                database.operationQueries.resetAllToPendingCreate(now = now, userId = userId)
            }
        }
    }

    override suspend fun downgradeToLocal(firebaseId: String, newLocalId: String, now: Long) {
        withContext(dispatchers.io) {
            database.transaction {

                database.operationQueries.deleteByUserId(userId = newLocalId)
                database.walletQueries.deleteByUserId(userId = newLocalId)

                database.walletQueries.downgradeToLocal(
                    newLocalId = newLocalId,
                    firebaseId = firebaseId,
                    now = now
                )
                database.operationQueries.downgradeToLocal(
                    newLocalId = newLocalId,
                    firebaseId = firebaseId,
                    now = now
                )
            }
        }
    }
}