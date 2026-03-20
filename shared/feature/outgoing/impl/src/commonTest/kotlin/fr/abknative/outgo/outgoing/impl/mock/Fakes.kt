package fr.abknative.outgo.outgoing.impl.mock

import fr.abknative.outgo.core.api.*
import fr.abknative.outgo.outgoing.api.model.Budget
import fr.abknative.outgo.outgoing.api.model.Outgoing
import fr.abknative.outgo.outgoing.api.repository.BudgetRepository
import fr.abknative.outgo.outgoing.api.repository.OutgoingRepository
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
    var outgoingToReturn: Outgoing? = null
    var lastSavedOutgoing: Outgoing? = null

    override suspend fun getOutgoingById(id: String): Outgoing? = outgoingToReturn

    override suspend fun insert(outgoing: Outgoing): Result<Unit, AppException> {
        lastSavedOutgoing = outgoing
        return Result.Success(Unit)
    }

    override suspend fun update(outgoing: Outgoing): Result<Unit, AppException> {
        lastSavedOutgoing = outgoing
        return Result.Success(Unit)
    }

    override fun observeOutgoingsByMonth(month: Int) = flowOf(emptyList<Outgoing>())
    override suspend fun markAsDeleted(id: String) = Result.Success(Unit)
}

class FakeBudgetRepository : BudgetRepository {
    private val _budgetFlow = MutableStateFlow<Budget?>(null)

    // Ces variables servent d'espions pour nos tests
    var lastInsertedBudget: Budget? = null
    var lastUpdatedBudget: Budget? = null
    var shouldReturnError = false

    fun emit(budget: Budget?) { _budgetFlow.value = budget }

    override fun observeBudget(id: String): Flow<Budget?> = _budgetFlow

    override suspend fun getBudget(id: String): Result<Budget?, AppException> {
        // Permet de tester le chemin "Error"
        if (shouldReturnError) return Result.Error(CommonError.DatabaseError())
        return Result.Success(_budgetFlow.value)
    }

    override suspend fun insert(budget: Budget): Result<Unit, AppException> {
        lastInsertedBudget = budget
        _budgetFlow.value = budget
        return Result.Success(Unit)
    }

    override suspend fun update(budget: Budget): Result<Unit, AppException> {
        lastUpdatedBudget = budget
        _budgetFlow.value = budget
        return Result.Success(Unit)
    }
}

class FakeKeyValueStorage : KeyValueStorage {
    private val storage = mutableMapOf<String, Any>()

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return storage[key] as? Boolean ?: defaultValue
    }

    override fun putBoolean(key: String, value: Boolean) {
        storage[key] = value
    }
}