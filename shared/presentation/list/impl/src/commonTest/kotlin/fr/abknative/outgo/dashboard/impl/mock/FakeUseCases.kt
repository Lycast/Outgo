package fr.abknative.outgo.dashboard.impl.mock

import fr.abknative.outgo.core.api.EpochMillis
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.wallet.api.model.Wallet
import fr.abknative.outgo.wallet.api.model.operation.OperationType
import fr.abknative.outgo.wallet.api.model.operation.Recurrence
import fr.abknative.outgo.wallet.api.model.presenter.PeriodStats
import fr.abknative.outgo.wallet.api.model.presenter.ProjectedOperation
import fr.abknative.outgo.wallet.api.usecase.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Fake implementation of [ObserveActiveOperationsUseCase] for testing purposes.
 */
class FakeObserveActiveOperationsUseCase : ObserveActiveOperationsUseCase {
    val outgoingsFlow = MutableStateFlow<List<ProjectedOperation>>(emptyList())

    override fun invoke(walletId: String, month: Int, year: Int): Flow<List<ProjectedOperation>> = outgoingsFlow
}

/**
 * Fake implementation of [SaveOperationUseCase] for testing purposes.
 */
class FakeSaveOperationUseCase : SaveOperationUseCase {
    var resultToReturn: Result<Unit, AppException> = Result.Success(Unit)

    override suspend fun invoke(
        id: String?, walletId: String, name: String, amountInCents: Long,
        type: OperationType, recurrence: Recurrence,
        startDate: EpochMillis, endDate: EpochMillis?
    ): Result<Unit, AppException> = resultToReturn
}

/**
 * Fake implementation of [DeleteOperationUseCase] for testing purposes.
 */
class FakeDeleteOperationUseCase : DeleteOperationUseCase {
    var resultToReturn: Result<Unit, AppException> = Result.Success(Unit)

    override suspend fun invoke(id: String): Result<Unit, AppException> = resultToReturn
}

/**
 * Fake implementation of [CalculatePeriodStatsUseCase] for testing purposes.
 */
class FakeCalculatePeriodStatsUseCase : CalculatePeriodStatsUseCase {
    var dataToReturn = PeriodStats(0L, 0L,0L, 0L, 0L)

    override fun invoke(operations: List<ProjectedOperation>, currentMonth: Int, currentYear: Int): PeriodStats {
        return dataToReturn
    }
}

/**
 * Fake implementation of [ObserveWalletsUseCase] for testing purposes.
 */
class FakeObserveWalletsUseCase : ObserveWalletsUseCase {
    private val walletsFlow = MutableStateFlow<List<Wallet>>(emptyList())

    fun emit(wallets: List<Wallet>) {
        walletsFlow.value = wallets
    }

    override fun invoke(): Flow<List<Wallet>> = walletsFlow
}

/**
 * Fake implementation of [SaveWalletUseCase] for testing purposes.
 */
class FakeSaveWalletUseCase : SaveWalletUseCase {
    var resultToReturn: Result<Unit, AppException> = Result.Success(Unit)

    override suspend fun invoke(id: String?, name: String): Result<Unit, AppException> {
        return resultToReturn
    }
}