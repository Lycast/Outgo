package fr.abknative.outgo.outgoing.impl.usecase

import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.outgoing.api.Recurrence
import fr.abknative.outgo.outgoing.api.repository.OutgoingRepository
import fr.abknative.outgo.outgoing.api.usecase.CalculateRemainingToPayUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class CalculateRemainingToPayUseCaseImpl(
    private val repository: OutgoingRepository,
    private val timeProvider: TimeProvider
) : CalculateRemainingToPayUseCase {

    override fun invoke(): Flow<Long> {
        return repository.observeActiveOutgoings().map { list ->
            val today = timeProvider.dayOfMonth()
            val thisMonth = timeProvider.monthValue()
            val lastDayOfThisMonth = timeProvider.lastDayOfMonth()

            list.filter { item ->
                val isImpactedThisMonth = when (item.recurrence) {
                    Recurrence.MONTHLY -> true
                    Recurrence.YEARLY -> item.dueMonth == thisMonth
                    Recurrence.UNKNOWN -> false
                }

                if (!isImpactedThisMonth) return@filter false

                val effectiveBillingDay = item.dueDay.coerceAtMost(lastDayOfThisMonth)
                effectiveBillingDay >= today

            }.sumOf { it.amountInCents }
        }
    }
}