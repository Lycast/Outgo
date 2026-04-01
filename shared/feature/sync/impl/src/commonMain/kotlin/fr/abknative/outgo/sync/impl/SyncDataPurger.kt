package fr.abknative.outgo.sync.impl

import fr.abknative.outgo.core.api.DataPurger
import fr.abknative.outgo.sync.api.SyncManager

internal class SyncDataPurger(
    private val syncManager: SyncManager
) : DataPurger {
    override suspend fun purgeData() {
        syncManager.clearSyncState()
    }
}