package fr.abknative.outgo.wallet.impl.usecase

import fr.abknative.outgo.core.api.IdProvider
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.wallet.api.model.operation.OperationType
import fr.abknative.outgo.wallet.api.model.operation.Recurrence
import fr.abknative.outgo.wallet.api.usecase.InitializeBudgetUseCase
import fr.abknative.outgo.wallet.api.usecase.SaveOperationUseCase
import fr.abknative.outgo.wallet.api.usecase.SaveWalletUseCase

class InitializeBudgetUseCaseImpl(
    private val saveWallet: SaveWalletUseCase,
    private val saveOperation: SaveOperationUseCase,
    private val idProvider: IdProvider,
    private val timeProvider: TimeProvider
) : InitializeBudgetUseCase {

    override suspend fun invoke(walletName: String, incomeInCents: Long): Result<Unit, AppException> {

        val newWalletId = idProvider.generate()

        val walletResult = saveWallet(id = newWalletId, name = walletName)
        if (walletResult is Result.Error) return walletResult

        val incomeResult = saveOperation(
            id = null,
            walletId = newWalletId,
            name = "Revenu Principal",
            amountInCents = incomeInCents,
            type = OperationType.INCOME,
            recurrence = Recurrence.MONTHLY,
            startDate = timeProvider.startOfMonth()
        )

        return incomeResult
    }
}