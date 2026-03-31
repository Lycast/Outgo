package fr.abknative.outgo.wallet.impl.usecase.engine

import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.wallet.api.model.Operation
import fr.abknative.outgo.wallet.api.model.dashboard.DashboardData
import fr.abknative.outgo.wallet.api.model.operation.OperationType

internal class SimpleSumEngine(
    private val timeProvider: TimeProvider
) : DashboardCalculationEngine {

    override fun calculate(operations: List<Operation>, currentMonth: Int, currentYear: Int): DashboardData {
        val realCurrentMonth = timeProvider.monthValue()
        val realCurrentYear = timeProvider.yearValue()

        val isViewingPast = currentYear < realCurrentYear || (currentYear == realCurrentYear && currentMonth < realCurrentMonth)
        val isViewingFuture = currentYear > realCurrentYear || (currentYear == realCurrentYear && currentMonth > realCurrentMonth)

        val today = timeProvider.dayOfMonth()

        val viewStartTimestamp = timeProvider.startOfMonth(currentMonth, currentYear)
        val lastDayOfViewedMonth = timeProvider.lastDayOfMonth(viewStartTimestamp)

        var totalIncome = 0L
        var totalExpenses = 0L
        var remainingToPay = 0L

        operations.forEach { operation ->
            when (operation.type) {
                OperationType.INCOME -> {
                    totalIncome += operation.amountInCents
                }
                OperationType.EXPENSE -> {
                    totalExpenses += operation.amountInCents

                    when {
                        isViewingPast -> { }
                        isViewingFuture -> {
                            remainingToPay += operation.amountInCents
                        }
                        else -> {
                            val anchorDay = timeProvider.dayOfMonth(operation.startDate)
                            val effectiveBillingDay = anchorDay.coerceAtMost(lastDayOfViewedMonth)

                            if (effectiveBillingDay >= today) {
                                remainingToPay += operation.amountInCents
                            }
                        }
                    }
                }
            }
        }

        val disposableIncome = totalIncome - totalExpenses

        return DashboardData(
            currentBalanceInCents = disposableIncome,
            totalExpensesInCents = totalExpenses,
            remainingToPayInCents = remainingToPay,
            disposableIncomeInCents = disposableIncome
        )
    }
}