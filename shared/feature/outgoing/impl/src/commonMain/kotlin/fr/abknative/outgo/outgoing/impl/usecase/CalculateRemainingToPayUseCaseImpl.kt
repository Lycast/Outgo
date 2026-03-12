package fr.abknative.outgo.outgoing.impl.usecase

import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.outgoing.api.model.Outgoing
import fr.abknative.outgo.outgoing.api.usecase.CalculateRemainingToPayUseCase

internal class CalculateRemainingToPayUseCaseImpl(
    private val timeProvider: TimeProvider
) : CalculateRemainingToPayUseCase {

    override fun invoke(outgoings: List<Outgoing>, selectedMonth: Int): Long {
        val currentRealMonth = timeProvider.monthValue()

        if (selectedMonth < currentRealMonth) {
            return 0L
        }
        if (selectedMonth > currentRealMonth) {
            return outgoings.sumOf { it.amountInCents }
        }

        val today = timeProvider.dayOfMonth()
        val lastDayOfThisMonth = timeProvider.lastDayOfMonth()

        return outgoings.filter { item ->
            val effectiveBillingDay = item.dueDay.coerceAtMost(lastDayOfThisMonth)
            effectiveBillingDay >= today
        }.sumOf { it.amountInCents }
    }
}