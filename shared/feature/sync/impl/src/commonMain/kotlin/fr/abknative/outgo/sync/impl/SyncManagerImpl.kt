package fr.abknative.outgo.sync.impl

import fr.abknative.outgo.core.api.KeyValueStorage
import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.outgoing.api.repository.BudgetRepository
import fr.abknative.outgo.outgoing.api.repository.OutgoingRepository
import fr.abknative.outgo.outgoing.network.SyncPushRequest
import fr.abknative.outgo.outgoing.network.mapper.toDomain
import fr.abknative.outgo.outgoing.network.mapper.toNetworkDto
import fr.abknative.outgo.sync.api.SyncManager
import fr.abknative.outgo.sync.api.SyncNetworkApi

internal class SyncManagerImpl(
    private val budgetRepository: BudgetRepository,
    private val outgoingRepository: OutgoingRepository,
    private val networkApi: SyncNetworkApi,
    private val storage: KeyValueStorage
) : SyncManager {

    companion object {
        private const val LAST_SYNC_KEY = "last_sync_timestamp"
    }

    override suspend fun syncAll(): Result<Unit, AppException> {
        // Règle d'or : On Push d'abord, on Pull ensuite.
        val pushResult = syncOut()
        if (pushResult is Result.Error) return pushResult

        return syncIn()
    }

    override suspend fun syncOut(): Result<Unit, AppException> {
        // 1. Collecter les données modifiées localement
        val pendingBudgets = budgetRepository.getPendingBudgets()
        val pendingOutgoings = outgoingRepository.getPendingOutgoings()

        if (pendingBudgets is Result.Error) return pendingBudgets
        if (pendingOutgoings is Result.Error) return pendingOutgoings

        val budgets = (pendingBudgets as Result.Success).data
        val outgoings = (pendingOutgoings as Result.Success).data

        if (budgets.isEmpty() && outgoings.isEmpty()) {
            return Result.Success(Unit) // Rien à envoyer, on gagne du temps
        }

        // 2. Transformer en DTOs et préparer le colis
        val request = SyncPushRequest(
            budgets = budgets.map { it.toNetworkDto() },
            outgoings = outgoings.map { it.toNetworkDto() }
        )

        // 3. Envoyer au serveur
        val pushResult = networkApi.pushData(request)
        if (pushResult is Result.Error) return pushResult

        // 4. Si le serveur dit "OK", on marque nos données locales comme SYNCED
        budgets.forEach { budgetRepository.updateSyncStatus(it.id, SyncStatus.SYNCED) }
        outgoings.forEach { outgoingRepository.updateSyncStatus(it.id, SyncStatus.SYNCED) }

        return Result.Success(Unit)
    }

    override suspend fun syncIn(): Result<Unit, AppException> {
        // 1. Lire à quand remonte la dernière synchro
        val lastSync = storage.getLong(LAST_SYNC_KEY, 0L)

        // 2. Demander les nouveautés au serveur
        val pullResult = networkApi.pullData(since = lastSync)
        if (pullResult is Result.Error) return pullResult

        val response = (pullResult as Result.Success).data

        // 3. Intégrer les nouveautés dans la base locale (Upsert)
        if (response.budgets.isNotEmpty()) {
            val domainBudgets = response.budgets.map { it.toDomain() }
            val budgetSyncResult = budgetRepository.syncFromServer(domainBudgets)
            if (budgetSyncResult is Result.Error) return budgetSyncResult
        }

        if (response.outgoings.isNotEmpty()) {
            val domainOutgoings = response.outgoings.map { it.toDomain() }
            val outgoingSyncResult = outgoingRepository.syncFromServer(domainOutgoings)
            if (outgoingSyncResult is Result.Error) return outgoingSyncResult
        }

        // 4. Enregistrer la date du serveur comme nouveau point de repère
        storage.putLong(LAST_SYNC_KEY, response.serverTimestamp)

        return Result.Success(Unit)
    }
}