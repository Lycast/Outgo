package fr.abknative.outgo.sync.impl

import fr.abknative.outgo.core.api.KeyValueStorage
import fr.abknative.outgo.core.api.NetworkMonitor
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.AppLogger
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _isSyncing = MutableStateFlow(false)
    override val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    companion object {
        private const val LAST_SYNC_KEY = "last_sync_timestamp"
    }

    override suspend fun syncAll(): Result<Unit, AppException> {
        AppLogger.get()?.i(tag, "Initiating full sync (syncAll)")

        if (!networkMonitor.isConnected.value) {
            AppLogger.get()?.w(tag, "Sync aborted: No network connection")
            return Result.Error(CommonError.NetworkError())
        }

        return syncMutex.withLock {
            _isSyncing.value = true
            try {
                val pushResult = doSyncOut()
                if (pushResult is Result.Error) {
                    AppLogger.get()?.e(tag, "Full sync failed during Push phase", pushResult.error)
                    return@withLock pushResult
                }

                val pullResult = doSyncIn()
                if (pullResult is Result.Error) {
                    AppLogger.get()?.e(tag, "Full sync failed during Pull phase", pullResult.error)
                    return@withLock pullResult
                }

                AppLogger.get()?.i(tag, "Full sync completed successfully")
                Result.Success(Unit)
            } finally {
                _isSyncing.value = false
            }
        }
    }

    override suspend fun syncOut(): Result<Unit, AppException> {
        return syncMutex.withLock {
            _isSyncing.value = true
            try { doSyncOut() } finally { _isSyncing.value = false }
        }
    }

    override suspend fun syncIn(): Result<Unit, AppException> {
        return syncMutex.withLock {
            _isSyncing.value = true
            try { doSyncIn() } finally { _isSyncing.value = false }
        }
    }

    private suspend fun doSyncOut(): Result<Unit, AppException> {
        AppLogger.get()?.d(tag, "Starting Push phase (doSyncOut)")
        if (!networkMonitor.isConnected.value) return Result.Success(Unit)

        val pendingWalletsResult = walletRepository.getPendingWallets()
        val pendingOperationsResult = operationRepository.getPendingOperations()

        if (pendingWalletsResult is Result.Error) return pendingWalletsResult
        if (pendingOperationsResult is Result.Error) return pendingOperationsResult

        val wallets = (pendingWalletsResult as Result.Success).data
        val operations = (pendingOperationsResult as Result.Success).data

        if (wallets.isEmpty() && operations.isEmpty()) {
            AppLogger.get()?.d(tag, "Nothing to push.")
            return Result.Success(Unit)
        }

        AppLogger.get()?.i(tag, "Pushing ${wallets.size} wallets and ${operations.size} operations...")

        val request = SyncPushRequest(
            wallets = wallets.map { it.toNetworkDto() },
            operations = operations.map { it.toNetworkDto() }
        )

        val pushResult = networkApi.pushData(request)
        if (pushResult is Result.Error) return pushResult

        wallets.forEach { walletRepository.updateSyncStatus(it.id, SyncStatus.SYNCED) }
        operations.forEach { operationRepository.updateSyncStatus(it.id, SyncStatus.SYNCED) }

        AppLogger.get()?.i(tag, "Push phase completed.")
        return Result.Success(Unit)
    }

    private suspend fun doSyncIn(): Result<Unit, AppException> {
        AppLogger.get()?.d(tag, "Starting Pull phase (doSyncIn)")
        if (!networkMonitor.isConnected.value) return Result.Success(Unit)

        val lastSync = storage.getLong(LAST_SYNC_KEY, 0L)

        val pullResult = networkApi.pullData(since = lastSync)
        if (pullResult is Result.Error) return pullResult

        val response = (pullResult as Result.Success).data

        if (response.wallets.isNotEmpty()) {
            AppLogger.get()?.d(tag, "Received ${response.wallets.size} wallets from server.")
            val domainWallets = response.wallets.map { it.toDomain() }
            val walletSyncResult = walletRepository.syncFromServer(domainWallets)
            if (walletSyncResult is Result.Error) return walletSyncResult
        }

        if (response.operations.isNotEmpty()) {
            AppLogger.get()?.d(tag, "Received ${response.operations.size} operations from server.")
            val domainOperations = response.operations.map { it.toDomain() }
            val operationSyncResult = operationRepository.syncFromServer(domainOperations)
            if (operationSyncResult is Result.Error) return operationSyncResult
        }

        storage.putLong(LAST_SYNC_KEY, response.serverTimestamp)
        AppLogger.get()?.i(tag, "Pull phase completed. Last sync timestamp updated.")

        return Result.Success(Unit)
    }

    override suspend fun hasRemoteData(): Result<Boolean, AppException> {
        AppLogger.get()?.d(tag, "Checking for remote data existence...")
        if (!networkMonitor.isConnected.value) {
            return Result.Error(CommonError.NetworkError())
        }

        val pullResult = networkApi.pullData(since = 0L)
        if (pullResult is Result.Error) {
            AppLogger.get()?.e(tag, "Failed to check remote data", pullResult.error)
            return pullResult
        }

        val response = (pullResult as Result.Success).data
        val hasData = response.wallets.isNotEmpty() || response.operations.isNotEmpty()

        AppLogger.get()?.d(tag, "Remote data exists: $hasData")
        return Result.Success(hasData)
    }

    override fun clearSyncState() {
        AppLogger.get()?.i(tag, "Clearing sync state (removing $LAST_SYNC_KEY)")
        storage.remove(LAST_SYNC_KEY)
    }
}