package fr.abknative.outgo.dashboard.impl.mock

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.sync.api.SyncManager

class FakeSyncManager : SyncManager {
    var syncAllCalled = false

    override suspend fun syncAll(): Result<Unit, AppException> {
        syncAllCalled = true
        return Result.Success(Unit)
    }

    override suspend fun syncOut(): Result<Unit, AppException> = Result.Success(Unit)

    override suspend fun syncIn(): Result<Unit, AppException> = Result.Success(Unit)

    override fun clearSyncState() {}
}