package fr.abknative.outgo.outgoing.impl.usecase

import fr.abknative.outgo.outgoing.api.repository.BudgetRepository
import fr.abknative.outgo.outgoing.api.usecase.CalculateDisposableIncomeUseCase
import fr.abknative.outgo.outgoing.api.usecase.CalculateTotalOutgoingsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

internal class CalculateDisposableIncomeUseCaseImpl(
    private val budgetRepository: BudgetRepository,
    private val calculateTotalOutgoingsUseCase: CalculateTotalOutgoingsUseCase
) : CalculateDisposableIncomeUseCase {

    override fun invoke(): Flow<Long> {
        return combine(
            budgetRepository.observeBudget().map { it.monthlyIncomeInCents },
            calculateTotalOutgoingsUseCase()
        ) { income, totalOutgoings ->
            income - totalOutgoings
        }
    }
}