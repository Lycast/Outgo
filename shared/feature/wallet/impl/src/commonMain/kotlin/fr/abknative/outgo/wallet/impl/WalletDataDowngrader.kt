package fr.abknative.outgo.wallet.impl

import fr.abknative.outgo.auth.api.provider.SessionProvider
import fr.abknative.outgo.core.api.AppDispatchers
import fr.abknative.outgo.core.api.LocalDataDowngrader
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.database.OutgoDatabase
import kotlinx.coroutines.withContext

internal class WalletDataDowngrader(
    private val database: OutgoDatabase,
    private val dispatchers: AppDispatchers,
    private val timeProvider: TimeProvider,
    private val sessionProvider: SessionProvider
) : LocalDataDowngrader {

    override suspend fun downgradeAllToPendingCreate() = withContext(dispatchers.io) {
        val uid = sessionProvider.getCurrentUserId()
        val now = timeProvider.now()

        database.transaction {
            database.walletQueries.resetAllToPendingCreate(now, uid)
            database.operationQueries.resetAllToPendingCreate(now, uid)
        }
    }
}