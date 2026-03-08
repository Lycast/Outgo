package fr.abknative.outgo.outgoing.impl.usecase

import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.outgoing.api.BillingCycle
import fr.abknative.outgo.outgoing.api.repository.OutgoingRepository
import fr.abknative.outgo.outgoing.api.usecase.CalculateRemainingToPayThisMonthUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class CalculateRemainingToPayThisMonthUseCaseImpl(
    private val repository: OutgoingRepository,
    private val timeProvider: TimeProvider
) : CalculateRemainingToPayThisMonthUseCase {

    override fun invoke(): Flow<Long> {
        return repository.observeActiveOutgoings().map { list ->
            val today = timeProvider.dayOfMonth()
            val thisMonth = timeProvider.monthValue()
            val lastDayOfThisMonth = timeProvider.lastDayOfMonth()

            list.filter { item ->
                val isDueThisMonth = when (item.cycle) {
                    BillingCycle.MONTHLY -> true
                    BillingCycle.YEARLY -> item.billingMonth == thisMonth
                    BillingCycle.UNKNOWN -> false
                }

                if (!isDueThisMonth) return@filter false

                val effectiveBillingDay = item.billingDay.coerceAtMost(lastDayOfThisMonth)

                effectiveBillingDay >= today

            }.sumOf { it.amountInCents }
        }
    }
}