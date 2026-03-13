package fr.abknative.outgo.outgoing.impl.usecase

import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.core.api.Result
import fr.abknative.outgo.outgoing.api.model.Budget
import fr.abknative.outgo.outgoing.api.repository.BudgetRepository
import fr.abknative.outgo.outgoing.api.usecase.UpdateIncomeUseCase

internal class UpdateIncomeUseCaseImpl(
    private val repository: BudgetRepository
) : UpdateIncomeUseCase {

    override suspend operator fun invoke(amountInCents: Long, budgetId: String): Result<Unit, AppException> {

        return when (val existingBudgetResult = repository.getBudget(budgetId)) {

            is Result.Success -> {
                val existingBudget = existingBudgetResult.data

                if (existingBudget == null) {
                    val newBudget = Budget(id = budgetId, monthlyIncomeInCents = amountInCents)
                    repository.insert(newBudget)
                } else {
                    val updatedBudget = existingBudget.copy(monthlyIncomeInCents = amountInCents)
                    repository.update(updatedBudget)
                }
            }

            is Result.Error -> {
                Result.Error(existingBudgetResult.error)
            }
        }
    }
}