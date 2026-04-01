package fr.abknative.outgo.dashboard.impl.mock

import fr.abknative.outgo.core.api.EpochMillis
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.wallet.api.model.Operation
import fr.abknative.outgo.wallet.api.model.Wallet
import fr.abknative.outgo.wallet.api.model.dashboard.DashboardData
import fr.abknative.outgo.wallet.api.model.operation.OperationType
import fr.abknative.outgo.wallet.api.model.operation.Recurrence
import fr.abknative.outgo.wallet.api.usecase.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeObserveActiveOperationsUseCase : ObserveActiveOperationsUseCase {
    val outgoingsFlow = MutableStateFlow<List<Operation>>(emptyList())
    override fun invoke(walletId: String, month: Int, year: Int): Flow<List<Operation>> = outgoingsFlow
}

class FakeSaveOperationUseCase : SaveOperationUseCase {
    var resultToReturn: Result<Unit, AppException> = Result.Success(Unit)
    override suspend fun invoke(
        id: String?, walletId: String, name: String, amountInCents: Long,
        type: OperationType, recurrence: Recurrence,
        startDate: EpochMillis, endDate: EpochMillis?
    ): Result<Unit, AppException> = resultToReturn
}

class FakeDeleteOperationUseCase : DeleteOperationUseCase {
    var resultToReturn: Result<Unit, AppException> = Result.Success(Unit)
    override suspend fun invoke(id: String): Result<Unit, AppException> = resultToReturn
}

class FakeCalculateDashboardDataUseCase : CalculateDashboardDataUseCase {
    var dataToReturn = DashboardData(0L, 0L, 0L, 0L)

    override fun invoke(operations: List<Operation>, currentMonth: Int, currentYear: Int): DashboardData {
        return dataToReturn
    }
}

class FakeObserveWalletsUseCase : ObserveWalletsUseCase {
    private val walletsFlow = MutableStateFlow<List<Wallet>>(emptyList())

    fun emit(wallets: List<Wallet>) {
        walletsFlow.value = wallets
    }

    override fun invoke(): Flow<List<Wallet>> = walletsFlow
}

class FakeSaveWalletUseCase : SaveWalletUseCase {
    var resultToReturn: Result<Unit, AppException> = Result.Success(Unit)

    override suspend fun invoke(id: String?, name: String): Result<Unit, AppException> {
        return resultToReturn
    }
}