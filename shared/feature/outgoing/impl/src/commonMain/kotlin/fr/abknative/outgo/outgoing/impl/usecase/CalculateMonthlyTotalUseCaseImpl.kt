package fr.abknative.outgo.outgoing.impl.usecase

import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.outgoing.api.BillingCycle
import fr.abknative.outgo.outgoing.api.repository.OutgoingRepository
import fr.abknative.outgo.outgoing.api.usecase.CalculateMonthlyTotalUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class CalculateMonthlyTotalUseCaseImpl(
    private val repository: OutgoingRepository,
    private val timeProvider: TimeProvider
) : CalculateMonthlyTotalUseCase {

    override fun invoke(): Flow<Long> {
        return repository.observeActiveOutgoings().map { list ->
            val currentMonthIndex = timeProvider.monthValue() // De 1 à 12

            list.sumOf { item ->
                when (item.cycle) {
                    BillingCycle.MONTHLY -> item.amountInCents

                    BillingCycle.YEARLY -> {
                        calculateYearlyShareForMonth(
                            totalYearlyInCents = item.amountInCents,
                            monthIndex = currentMonthIndex
                        )
                    }

                    BillingCycle.UNKNOWN -> 0L
                }
            }
        }
    }

    /**
     * Calculates the exact portion of an annual amount for a given month (1-12).
     * Distributes any remaining cents across the first months of the year.
     */
    private fun calculateYearlyShareForMonth(
        totalYearlyInCents: Long,
        monthIndex: Int
    ): Long {
        val baseMonthly = totalYearlyInCents / 12
        val remainder = totalYearlyInCents % 12

        // If the current month is within the first 'X' months (where X is the remainder),
        // add a 1-cent "bonus".
        return if (monthIndex <= remainder) {
            baseMonthly + 1
        } else {
            baseMonthly
        }
    }
}