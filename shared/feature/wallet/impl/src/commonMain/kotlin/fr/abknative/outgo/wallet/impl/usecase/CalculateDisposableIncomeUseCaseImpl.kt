package fr.abknative.outgo.wallet.impl.usecase

import fr.abknative.outgo.wallet.api.usecase.CalculateDisposableIncomeUseCase

internal class CalculateDisposableIncomeUseCaseImpl : CalculateDisposableIncomeUseCase {

    override fun invoke(incomeInCents: Long, totalOutgoingsInCents: Long): Long {
        return incomeInCents - totalOutgoingsInCents
    }
}