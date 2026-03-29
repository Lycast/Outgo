package fr.abknative.outgo.dashboard.impl.mock

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.wallet.api.Recurrence
import fr.abknative.outgo.wallet.api.model.Outgoing
import fr.abknative.outgo.wallet.api.usecase.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeObserveActiveOutgoingsUseCase : ObserveActiveOutgoingsUseCase {
    val outgoingsFlow = MutableStateFlow<List<Outgoing>>(emptyList())
    override fun invoke(month: Int): Flow<List<Outgoing>> = outgoingsFlow
}

class FakeSaveOutgoingUseCase : SaveOutgoingUseCase {
    var resultToReturn: Result<Unit, AppException> = Result.Success(Unit)
    override suspend fun invoke(
        id: String?, name: String, amountInCents: Long,
        recurrence: Recurrence, dueDay: Int, dueMonth: Int?
    ): Result<Unit, AppException> = resultToReturn
}

class FakeDeleteOutgoingUseCase : DeleteOutgoingUseCase {
    var resultToReturn: Result<Unit, AppException> = Result.Success(Unit)
    override suspend fun invoke(id: String): Result<Unit, AppException> = resultToReturn
}

class FakeCalculateTotalOutgoingsUseCase : CalculateTotalOutgoingsUseCase {
    var totalToReturn: Long = 0L
    override fun invoke(outgoings: List<Outgoing>): Long = totalToReturn
}

class FakeCalculateRemainingToPayUseCase : CalculateRemainingToPayUseCase {
    var remainingToReturn: Long = 0L
    override fun invoke(outgoings: List<Outgoing>, selectedMonth: Int): Long = remainingToReturn
}

class FakeCalculateDisposableIncomeUseCase : CalculateDisposableIncomeUseCase {
    var disposableToReturn: Long = 0L
    override fun invoke(incomeInCents: Long, totalOutgoingsInCents: Long): Long = disposableToReturn
}

class FakeUpdateIncomeUseCase : UpdateIncomeUseCase {
    override suspend fun invoke(amountInCents: Long, budgetId: String): Result<Unit, AppException> {
        return Result.Success(Unit)
    }
}