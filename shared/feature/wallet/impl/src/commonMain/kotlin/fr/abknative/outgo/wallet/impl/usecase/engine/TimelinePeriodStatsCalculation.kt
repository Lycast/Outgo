package fr.abknative.outgo.wallet.impl.usecase.engine

import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.wallet.api.model.operation.OperationType
import fr.abknative.outgo.wallet.api.model.presenter.PeriodStats
import fr.abknative.outgo.wallet.api.model.presenter.ProjectedOperation

internal class TimelinePeriodStatsCalculation(
    private val timeProvider: TimeProvider
) : PeriodStatsCalculation {

    override fun calculate(
        operations: List<ProjectedOperation>,
        currentMonth: Int,
        currentYear: Int
    ): PeriodStats {
        val realCurrentMonth = timeProvider.monthValue()
        val realCurrentYear = timeProvider.yearValue()

        val isViewingPast = currentYear < realCurrentYear || (currentYear == realCurrentYear && currentMonth < realCurrentMonth)
        val isViewingFuture = currentYear > realCurrentYear || (currentYear == realCurrentYear && currentMonth > realCurrentMonth)

        val today = timeProvider.dayOfMonth()

        var totalIncome = 0L
        var totalExpenses = 0L
        var remainingToPay = 0L

        operations.forEach { projected ->
            val op = projected.operation
            val amount = op.amountInCents

            when (op.type) {
                OperationType.INCOME -> {
                    totalIncome += amount
                }
                OperationType.EXPENSE -> {
                    totalExpenses += amount

                    when {
                        isViewingPast -> { /* Tout est payé */ }
                        isViewingFuture -> {
                            remainingToPay += amount
                        }
                        else -> {
                            val projectedDay = timeProvider.dayOfMonth(projected.projectedDate)

                            if (projectedDay > today) {
                                remainingToPay += amount
                            }
                        }
                    }
                }
            }
        }

        val disposableIncome = totalIncome - totalExpenses

        return PeriodStats(
            currentBalanceInCents = disposableIncome,
            totalIncomesInCents = totalIncome,
            totalExpensesInCents = totalExpenses,
            remainingToPayInCents = remainingToPay,
            disposableIncomeInCents = disposableIncome
        )
    }
}