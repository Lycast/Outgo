package fr.abknative.outgo.outgoing.impl.usecase

import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.core.api.Result
import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.outgoing.api.model.Budget
import fr.abknative.outgo.outgoing.api.repository.BudgetRepository
import fr.abknative.outgo.outgoing.api.usecase.UpdateIncomeUseCase


internal class UpdateIncomeUseCaseImpl(
    private val repository: BudgetRepository,
    private val timeProvider: TimeProvider
) : UpdateIncomeUseCase {

    override suspend operator fun invoke(amountInCents: Long, budgetId: String): Result<Unit, AppException> {
        return when (val existingResult = repository.getBudget(budgetId)) {
            is Result.Success -> {
                val existing = existingResult.data
                val currentTime = timeProvider.now()

                if (existing == null) {
                    val newBudget = Budget(
                        id = budgetId,
                        monthlyIncomeInCents = amountInCents,
                        createdAt = currentTime,
                        updatedAt = currentTime,
                        syncStatus = SyncStatus.PENDING_CREATE
                    )
                    repository.insert(newBudget)
                } else {
                    val newStatus = if (existing.syncStatus == SyncStatus.SYNCED) {
                        SyncStatus.PENDING_UPDATE
                    } else {
                        existing.syncStatus
                    }

                    val updatedBudget = existing.copy(
                        monthlyIncomeInCents = amountInCents,
                        updatedAt = currentTime,
                        syncStatus = newStatus
                    )
                    repository.update(updatedBudget)
                }
            }
            is Result.Error -> Result.Error(existingResult.error)
        }
    }
}