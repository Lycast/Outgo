package fr.abknative.outgo.outgoing.impl.usecase

import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.outgoing.api.Recurrence
import fr.abknative.outgo.outgoing.api.repository.OutgoingRepository
import fr.abknative.outgo.outgoing.api.usecase.CalculateTotalOutgoingsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class CalculateTotalOutgoingsUseCaseImpl(
    private val repository: OutgoingRepository,
    private val timeProvider: TimeProvider
) : CalculateTotalOutgoingsUseCase {

    override fun invoke(): Flow<Long> {
        return repository.observeActiveOutgoings().map { list ->
            val thisMonth = timeProvider.monthValue()

            list.sumOf { item ->
                val isImpactedThisMonth = when (item.recurrence) {
                    Recurrence.MONTHLY -> true
                    Recurrence.YEARLY -> item.dueMonth == thisMonth
                    Recurrence.UNKNOWN -> false
                }

                if (isImpactedThisMonth) item.amountInCents else 0L
            }
        }
    }
}