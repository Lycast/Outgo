package fr.abknative.outgo.outgoing.api.usecase

/**
 * Encapsulates the business rule for calculating the user's disposable income.
 */
interface CalculateDisposableIncomeUseCase {

    /**
     * Calculates the remaining income after deducting all smoothed expenses.
     *
     * @param incomeInCents The total monthly income or baseline budget.
     * @param totalOutgoingsInCents The sum of all calculated expenses for the month.
     * @return The disposable income available, in cents.
     */
    operator fun invoke(incomeInCents: Long, totalOutgoingsInCents: Long): Long
}