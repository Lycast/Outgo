package fr.abknative.outgo.outgoing.impl.usecase

import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.core.api.Result
import fr.abknative.outgo.outgoing.api.OutgoingError
import fr.abknative.outgo.outgoing.api.repository.BudgetRepository
import fr.abknative.outgo.outgoing.api.usecase.UpdateIncomeUseCase

internal class UpdateIncomeUseCaseImpl(
    private val repository: BudgetRepository
) : UpdateIncomeUseCase {

    override suspend fun invoke(amountInCents: Long): Result<Unit, AppException> {
        if (amountInCents < 0) return Result.Error(OutgoingError.InvalidAmount())

        return repository.updateMonthlyIncome(amountInCents)
    }
}