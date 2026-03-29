package fr.abknative.outgo.wallet.impl.usecase

import fr.abknative.outgo.wallet.api.model.Outgoing
import fr.abknative.outgo.wallet.api.usecase.CalculateTotalOutgoingsUseCase

internal class CalculateTotalOutgoingsUseCaseImpl : CalculateTotalOutgoingsUseCase {

    override fun invoke(outgoings: List<Outgoing>): Long {
        return outgoings.sumOf { it.amountInCents }
    }
}