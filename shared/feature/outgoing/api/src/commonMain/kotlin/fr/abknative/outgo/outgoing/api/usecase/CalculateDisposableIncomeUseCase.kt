package fr.abknative.outgo.outgoing.api.usecase

interface CalculateDisposableIncomeUseCase {
    /**
     * Calculates the "remaining income" (Monthly income - Total smoothed expenses).
     * @param incomeInCents Le revenu total (ou budget).
     * @param totalOutgoingsInCents Le total des charges calculées pour le mois.
     */
    operator fun invoke(incomeInCents: Long, totalOutgoingsInCents: Long): Long
}