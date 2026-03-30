package fr.abknative.outgo.wallet.impl.mock

import fr.abknative.outgo.core.api.EpochMillis
import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.CommonError
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.wallet.api.model.Operation
import fr.abknative.outgo.wallet.api.model.Wallet
import fr.abknative.outgo.wallet.api.repository.BudgetRepository
import fr.abknative.outgo.wallet.api.repository.OutgoingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class FakeTimeProvider : TimeProvider {
    var mockedNow: EpochMillis = 0L
    var mockedDay: Int = 1
    var mockedMonth: Int = 1
    var mockedLastDay: Int = 31

    override fun now(): EpochMillis = mockedNow
    override fun dayOfMonth(ts: EpochMillis): Int = mockedDay
    override fun monthValue(ts: EpochMillis): Int = mockedMonth
    override fun lastDayOfMonth(ts: EpochMillis): Int = mockedLastDay

    override fun yearValue(ts: EpochMillis): Int = 2026
    override fun hourOf(ts: EpochMillis): Int = 0
    override fun minuteOf(ts: EpochMillis): Int = 0
    override fun plusDays(base: EpochMillis, days: Int): EpochMillis = base
    override fun minusDays(base: EpochMillis, days: Int): EpochMillis = base
    override fun startOfMonth(ts: EpochMillis): EpochMillis = 0L
    override fun endOfMonth(ts: EpochMillis): EpochMillis = 0L
    override fun isSameDay(ts1: EpochMillis, ts2: EpochMillis): Boolean = ts1 == ts2
    override fun isWeekend(ts: EpochMillis): Boolean = false
    override fun combineDateAndTime(dateEpochMillis: EpochMillis, hour: Int, minute: Int): EpochMillis = 0L
}

class FakeOutgoingRepository : OutgoingRepository {
    // Simule la table OutgoingEntity
    private val outgoingsMap = mutableMapOf<String, Operation>()

    var operationToReturn: Operation? = null
    var lastSavedOperation: Operation? = null

    override suspend fun getOutgoingById(id: String): Operation? = outgoingsMap[id] ?: operationToReturn

    override suspend fun insert(operation: Operation): Result<Unit, AppException> {
        outgoingsMap[operation.id] = operation
        lastSavedOperation = operation
        return Result.Success(Unit)
    }

    override suspend fun update(operation: Operation): Result<Unit, AppException> {
        outgoingsMap[operation.id] = operation
        lastSavedOperation = operation
        return Result.Success(Unit)
    }

    override fun observeOutgoingsByMonth(month: Int): Flow<List<Operation>> = flowOf(outgoingsMap.values.toList())

    override suspend fun markAsDeleted(id: String): Result<Unit, AppException> {
        outgoingsMap[id]?.let { outgoingsMap[id] = it.copy(isDeleted = true, syncStatus = SyncStatus.PENDING_DELETE) }
        return Result.Success(Unit)
    }

    override suspend fun syncFromServer(operations: List<Operation>): Result<Unit, AppException> {
        operations.forEach {
            outgoingsMap[it.id] = it.copy(syncStatus = SyncStatus.SYNCED)
        }
        return Result.Success(Unit)
    }

    // N'oublie pas de vérifier les autres méthodes qui utilisent cette Map
    override suspend fun updateSyncStatus(id: String, status: SyncStatus): Result<Unit, AppException> {
        outgoingsMap[id]?.let { outgoingsMap[id] = it.copy(syncStatus = status) }
        return Result.Success(Unit)
    }

    override suspend fun getPendingOutgoings(): Result<List<Operation>, AppException> {
        return Result.Success(outgoingsMap.values.filter { it.syncStatus != SyncStatus.SYNCED })
    }
}

class FakeBudgetRepository : BudgetRepository {
    private val _walletFlow = MutableStateFlow<Wallet?>(null)
    private val budgets = mutableMapOf<String, Wallet>()

    var lastInsertedWallet: Wallet? = null
    var lastUpdatedWallet: Wallet? = null
    var shouldReturnError = false

    fun emit(wallet: Wallet?) {
        wallet?.let { budgets[it.id] = it }
        _walletFlow.value = wallet
    }

    override fun observeBudget(id: String): Flow<Wallet?> = _walletFlow

    override suspend fun getBudget(id: String): Result<Wallet?, AppException> {
        if (shouldReturnError) return Result.Error(CommonError.DatabaseError())
        return Result.Success(budgets[id] ?: _walletFlow.value)
    }

    override suspend fun insert(wallet: Wallet): Result<Unit, AppException> {
        lastInsertedWallet = wallet
        emit(wallet)
        return Result.Success(Unit)
    }

    override suspend fun update(wallet: Wallet): Result<Unit, AppException> {
        lastUpdatedWallet = wallet
        emit(wallet)
        return Result.Success(Unit)
    }

    override suspend fun getPendingBudgets(): Result<List<Wallet>, AppException> {
        return Result.Success(budgets.values.filter { it.syncStatus != SyncStatus.SYNCED })
    }

    override suspend fun updateSyncStatus(id: String, status: SyncStatus): Result<Unit, AppException> {
        budgets[id]?.let { emit(it.copy(syncStatus = status)) }
        return Result.Success(Unit)
    }

    override suspend fun syncFromServer(wallets: List<Wallet>): Result<Unit, AppException> {
        wallets.forEach { emit(it.copy(syncStatus = SyncStatus.SYNCED)) }
        return Result.Success(Unit)
    }
}