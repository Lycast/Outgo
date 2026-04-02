package fr.abknative.outgo.wallet.impl.usecase.engine

import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.wallet.api.model.dashboard.DashboardData
import fr.abknative.outgo.wallet.api.model.dashboard.ProjectedOperation
import fr.abknative.outgo.wallet.api.model.operation.OperationType

internal class SimpleSumEngine(
    private val timeProvider: TimeProvider
) : DashboardCalculationEngine {

    override fun calculate(
        operations: List<ProjectedOperation>,
        currentMonth: Int,
        currentYear: Int
    ): DashboardData {
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

                            if (projectedDay >= today) {
                                remainingToPay += amount
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