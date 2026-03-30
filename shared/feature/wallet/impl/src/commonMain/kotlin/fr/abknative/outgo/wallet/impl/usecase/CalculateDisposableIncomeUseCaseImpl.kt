package fr.abknative.outgo.wallet.impl.usecase

internal class CalculateDisposableIncomeUseCaseImpl : CalculateDisposableIncomeUseCase {

    override fun invoke(incomeInCents: Long, totalOutgoingsInCents: Long): Long {
        return incomeInCents - totalOutgoingsInCents
    }
}