package fr.abknative.outgo.sync.impl

import fr.abknative.outgo.core.api.KeyValueStorage
import fr.abknative.outgo.core.api.NetworkMonitor
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.CommonError
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.core.api.model.SyncStatus
import fr.abknative.outgo.sync.api.SyncManager
import fr.abknative.outgo.sync.api.SyncNetworkApi
import fr.abknative.outgo.wallet.api.repository.OperationRepository
import fr.abknative.outgo.wallet.api.repository.WalletRepository
import fr.abknative.outgo.wallet.network.SyncPushRequest
import fr.abknative.outgo.wallet.network.mapper.toDomain
import fr.abknative.outgo.wallet.network.mapper.toNetworkDto
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class SyncManagerImpl(
    private val walletRepository: WalletRepository,
    private val operationRepository: OperationRepository,
    private val networkApi: SyncNetworkApi,
    private val networkMonitor: NetworkMonitor,
    private val storage: KeyValueStorage
) : SyncManager {

    private val syncMutex = Mutex()
    private val tag = "SyncManager"

    companion object {
        private const val LAST_SYNC_KEY = "last_sync_timestamp"
    }

    override suspend fun syncAll(): Result<Unit, AppException> {
        if (!networkMonitor.isConnected.value) {
            return Result.Error(CommonError.NetworkError())
        }

        return syncMutex.withLock {
            val pushResult = syncOut()
            if (pushResult is Result.Error) return pushResult

            syncIn()
        }
    }

    override suspend fun syncOut(): Result<Unit, AppException> {
        if (!networkMonitor.isConnected.value) return Result.Success(Unit)

        val pendingWalletsResult = walletRepository.getPendingWallets()
        val pendingOperationsResult = operationRepository.getPendingOperations()

        if (pendingWalletsResult is Result.Error) return pendingWalletsResult
        if (pendingOperationsResult is Result.Error) return pendingOperationsResult

        val wallets = (pendingWalletsResult as Result.Success).data
        val operations = (pendingOperationsResult as Result.Success).data

        if (wallets.isEmpty() && operations.isEmpty()) {
            return Result.Success(Unit)
        }

        val request = SyncPushRequest(
            wallets = wallets.map { it.toNetworkDto() },
            operations = operations.map { it.toNetworkDto() }
        )

        val pushResult = networkApi.pushData(request)
        if (pushResult is Result.Error) return pushResult

        wallets.forEach { walletRepository.updateSyncStatus(it.id, SyncStatus.SYNCED) }
        operations.forEach { operationRepository.updateSyncStatus(it.id, SyncStatus.SYNCED) }

        return Result.Success(Unit)
    }

    override suspend fun syncIn(): Result<Unit, AppException> {
        if (!networkMonitor.isConnected.value) return Result.Success(Unit)

        val lastSync = storage.getLong(LAST_SYNC_KEY, 0L)

        val pullResult = networkApi.pullData(since = lastSync)
        if (pullResult is Result.Error) return pullResult

        val response = (pullResult as Result.Success).data

        if (response.wallets.isNotEmpty()) {
            val domainWallets = response.wallets.map { it.toDomain() }
            val walletSyncResult = walletRepository.syncFromServer(domainWallets)
            if (walletSyncResult is Result.Error) return walletSyncResult
        }

        if (response.operations.isNotEmpty()) {
            val domainOperations = response.operations.map { it.toDomain() }
            val operationSyncResult = operationRepository.syncFromServer(domainOperations)
            if (operationSyncResult is Result.Error) return operationSyncResult
        }

        storage.putLong(LAST_SYNC_KEY, response.serverTimestamp)

        return Result.Success(Unit)
    }

    override suspend fun hasRemoteData(): Result<Boolean, AppException> {
        if (!networkMonitor.isConnected.value) {
            return Result.Error(CommonError.NetworkError())
        }

        val pullResult = networkApi.pullData(since = 0L)
        if (pullResult is Result.Error) return pullResult

        val response = (pullResult as Result.Success).data

        val hasData = response.wallets.isNotEmpty() || response.operations.isNotEmpty()

        return Result.Success(hasData)
    }

    override fun clearSyncState() {
        storage.remove(LAST_SYNC_KEY)
    }
}