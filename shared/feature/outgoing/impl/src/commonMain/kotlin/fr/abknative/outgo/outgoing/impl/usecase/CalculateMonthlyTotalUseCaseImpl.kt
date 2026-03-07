package fr.abknative.outgo.outgoing.impl.usecase

import fr.abknative.outgo.outgoing.api.BillingCycle
import fr.abknative.outgo.outgoing.api.repository.OutgoingRepository
import fr.abknative.outgo.outgoing.api.usecase.CalculateMonthlyTotalUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class CalculateMonthlyTotalUseCaseImpl(
    private val repository: OutgoingRepository
) : CalculateMonthlyTotalUseCase {

    override fun invoke(): Flow<Long> {
        return repository.observeActiveOutgoings().map { list ->
            list.sumOf { item ->
                when (item.cycle) {
                    BillingCycle.MONTHLY -> item.amountInCents
                    BillingCycle.YEARLY -> item.amountInCents / 12
                    BillingCycle.UNKNOWN -> 0L
                }
            }
        }
    }
}