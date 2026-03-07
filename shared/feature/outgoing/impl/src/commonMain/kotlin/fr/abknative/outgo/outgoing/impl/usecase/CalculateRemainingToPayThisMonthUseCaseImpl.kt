package fr.abknative.outgo.outgoing.impl.usecase

import fr.abknative.outgo.outgoing.api.BillingCycle
import fr.abknative.outgo.outgoing.api.repository.OutgoingRepository
import fr.abknative.outgo.outgoing.api.usecase.CalculateRemainingToPayThisMonthUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class CalculateRemainingToPayThisMonthUseCaseImpl(
    private val repository: OutgoingRepository
) : CalculateRemainingToPayThisMonthUseCase {

    override fun invoke(currentDayOfMonth: Int): Flow<Long> {
        return repository.observeActiveOutgoings().map { list ->
            list.filter { item ->
                item.cycle == BillingCycle.MONTHLY && item.billingDay > currentDayOfMonth
            }.sumOf { it.amountInCents }
        }
    }
}