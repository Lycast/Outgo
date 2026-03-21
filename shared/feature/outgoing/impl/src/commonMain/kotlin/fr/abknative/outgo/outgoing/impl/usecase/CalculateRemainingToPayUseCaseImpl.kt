package fr.abknative.outgo.outgoing.impl.usecase

import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.outgoing.api.model.Outgoing
import fr.abknative.outgo.outgoing.api.usecase.CalculateRemainingToPayUseCase

internal class CalculateRemainingToPayUseCaseImpl(
    private val timeProvider: TimeProvider
) : CalculateRemainingToPayUseCase {

    /**
     * Implementation details:
     * - Past months: Returns 0 (assumes all bills are already paid).
     * - Future months: Returns the total sum of all bills.
     * - Current month: Filters bills whose effective billing day is today or later.
     * Safely handles end-of-month edge cases (e.g., a bill due on the 31st during February)
     * by coercing the due day to the last valid day of the current month.
     */
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