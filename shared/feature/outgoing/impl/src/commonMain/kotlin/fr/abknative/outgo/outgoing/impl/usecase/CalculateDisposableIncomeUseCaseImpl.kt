package fr.abknative.outgo.outgoing.impl.usecase

import fr.abknative.outgo.outgoing.api.usecase.CalculateDisposableIncomeUseCase

internal class CalculateDisposableIncomeUseCaseImpl : CalculateDisposableIncomeUseCase {

    override fun invoke(incomeInCents: Long, totalOutgoingsInCents: Long): Long {
        return incomeInCents - totalOutgoingsInCents
    }
}