package fr.abknative.outgo.outgoing.impl.usecase

import fr.abknative.outgo.outgoing.api.model.Outgoing
import fr.abknative.outgo.outgoing.api.usecase.CalculateTotalOutgoingsUseCase

internal class CalculateTotalOutgoingsUseCaseImpl : CalculateTotalOutgoingsUseCase {

    override fun invoke(outgoings: List<Outgoing>): Long {
        return outgoings.sumOf { it.amountInCents }
    }
}