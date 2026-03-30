package fr.abknative.outgo.sync.impl

import fr.abknative.outgo.core.api.KeyValueStorage
import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
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
    private val storage: KeyValueStorage
) : SyncManager {

    private val syncMutex = Mutex()

    companion object {
        private const val LAST_SYNC_KEY = "last_sync_timestamp"
    }

    override suspend fun syncAll(): Result<Unit, AppException> {
        return syncMutex.withLock {
            val pushResult = syncOut()
            if (pushResult is Result.Error) return pushResult

            syncIn()
        }
    }

    override suspend fun syncOut(): Result<Unit, AppException> {
        val pendingBudgets = walletRepository.getPendingBudgets()
        val pendingOutgoings = operationRepository.getPendingOutgoings()

        if (pendingBudgets is Result.Error) return pendingBudgets
        if (pendingOutgoings is Result.Error) return pendingOutgoings

        val budgets = (pendingBudgets as Result.Success).data
        val outgoings = (pendingOutgoings as Result.Success).data

        if (budgets.isEmpty() && outgoings.isEmpty()) {
            return Result.Success(Unit)
        }

        val request = SyncPushRequest(
            budgets = budgets.map { it.toNetworkDto() },
            outgoings = outgoings.map { it.toNetworkDto() }
        )

        val pushResult = networkApi.pushData(request)
        if (pushResult is Result.Error) return pushResult

        budgets.forEach { walletRepository.updateSyncStatus(it.id, SyncStatus.SYNCED) }
        outgoings.forEach { operationRepository.updateSyncStatus(it.id, SyncStatus.SYNCED) }

        return Result.Success(Unit)
    }

    override suspend fun syncIn(): Result<Unit, AppException> {
        val lastSync = storage.getLong(LAST_SYNC_KEY, 0L)

        val pullResult = networkApi.pullData(since = lastSync)
        if (pullResult is Result.Error) return pullResult

        val response = (pullResult as Result.Success).data

        if (response.budgets.isNotEmpty()) {
            val domainBudgets = response.budgets.map { it.toDomain() }
            val budgetSyncResult = walletRepository.syncFromServer(domainBudgets)
            if (budgetSyncResult is Result.Error) return budgetSyncResult
        }

        if (response.outgoings.isNotEmpty()) {
            val domainOutgoings = response.outgoings.map { it.toDomain() }
            val outgoingSyncResult = operationRepository.syncFromServer(domainOutgoings)
            if (outgoingSyncResult is Result.Error) return outgoingSyncResult
        }

        storage.putLong(LAST_SYNC_KEY, response.serverTimestamp)

        return Result.Success(Unit)
    }
}