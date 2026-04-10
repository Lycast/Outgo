package fr.abknative.outgo.sync.impl

import fr.abknative.outgo.auth.api.provider.SessionProvider
import fr.abknative.outgo.core.api.DataPurger
import fr.abknative.outgo.sync.api.SyncManager

internal class SyncDataPurger(
    private val syncManager: SyncManager,
    private val sessionProvider: SessionProvider
) : DataPurger {
    override suspend fun purgeData(userId: String?) {
        val currentId = sessionProvider.getCurrentUserId()

        if (userId == null || userId == currentId) {
            syncManager.clearSyncState()
        }
    }
}