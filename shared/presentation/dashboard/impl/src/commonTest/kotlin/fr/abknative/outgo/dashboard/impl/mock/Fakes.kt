package fr.abknative.outgo.dashboard.impl.mock

import fr.abknative.outgo.auth.api.model.UserSession
import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.core.api.EpochMillis
import fr.abknative.outgo.core.api.KeyValueStorage
import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.CommonError
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.sync.api.SyncManager
import fr.abknative.outgo.wallet.api.model.Wallet
import fr.abknative.outgo.wallet.api.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

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

class FakeWalletRepository : WalletRepository {
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

class FakeAuthRepository : AuthRepository {
    private val _sessionFlow = MutableStateFlow<UserSession?>(null)

    // Permet de simuler une connexion/déconnexion dans les tests
    fun emit(session: UserSession?) {
        _sessionFlow.value = session
    }

    override fun observeSession(): Flow<UserSession?> = _sessionFlow

    override suspend fun getSession(): UserSession? = _sessionFlow.value

    override suspend fun login(email: String, password: String) {}

    override suspend fun logout() {
        _sessionFlow.value = null
    }
}

class FakeSyncManager : SyncManager {
    var syncAllCalled = false

    override suspend fun syncAll(): Result<Unit, AppException> {
        syncAllCalled = true
        return Result.Success(Unit)
    }

    override suspend fun syncOut(): Result<Unit, AppException> = Result.Success(Unit)

    override suspend fun syncIn(): Result<Unit, AppException> = Result.Success(Unit)
}

class FakeKeyValueStorage : KeyValueStorage {
    private val storage = mutableMapOf<String, Any>()

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return storage[key] as? Boolean ?: defaultValue
    }

    override fun putBoolean(key: String, value: Boolean) {
        storage[key] = value
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return storage[key] as? Long ?: defaultValue
    }

    override fun putLong(key: String, value: Long) {
        storage[key] = value
    }

    override fun getString(key: String): String? {
        TODO("Not yet implemented")
    }

    override fun putString(key: String, value: String) {
        TODO("Not yet implemented")
    }

    override fun remove(key: String) {
        TODO("Not yet implemented")
    }
}