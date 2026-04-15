package fr.abknative.outgo.month.impl

import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.month.api.MonthState
import fr.abknative.outgo.wallet.api.model.operation.OperationType
import fr.abknative.outgo.wallet.api.model.presenter.PeriodStats
import fr.abknative.outgo.wallet.api.model.presenter.ProjectedOperation

internal class MonthStateMapper(private val timeProvider: TimeProvider) {

    fun mapToState(
        currentState: MonthState,
        currentOperations: List<ProjectedOperation>,
        stats: PeriodStats,
        input: MonthPipelineInput
    ): MonthState {

        val expensesOnly = currentOperations.filter { it.operation.type == OperationType.EXPENSE }
        val incomeOp = currentOperations.firstOrNull { it.operation.type == OperationType.INCOME }

        return currentState.copy(
            isLoading = false,
            activeWalletId = input.wallet.id,
            activeWalletName = input.wallet.name,
            incomeOperationId = incomeOp?.operation?.id,
            incomeOperationName = incomeOp?.operation?.name ?: "Revenu",
            incomeOperationStartDate = incomeOp?.operation?.startDate,
            monthlyIncomeInCents = incomeOp?.operation?.amountInCents ?: 0L,
            selectedMonth = input.month,
            selectedYear = input.year,
            canGoToPreviousMonth = calculateCanGoBack(input),
            totalOutgoingsInCents = stats.totalExpensesInCents,
            remainingToPayInCents = stats.remainingToPayInCents,
            disposableIncomeInCents = stats.disposableIncomeInCents,
            expensesByRecurrence = expensesOnly
                .groupBy { it.operation.recurrence }
                .mapValues { (_, ops) -> ops.sumOf { it.operation.amountInCents } },
            nextUpcomingExpenses = calculateUpcomingExpenses(expensesOnly, input),
            error = null
        )
    }

    private fun calculateUpcomingExpenses(
        expenses: List<ProjectedOperation>,
        input: MonthPipelineInput
    ): List<ProjectedOperation> {
        val currentDay = timeProvider.dayOfMonth()
        val nowMonth = timeProvider.monthValue()
        val nowYear = timeProvider.yearValue()

        val viewAbsolute = input.year * 12 + input.month
        val nowAbsolute = nowYear * 12 + nowMonth

        return when {
            viewAbsolute < nowAbsolute -> emptyList()
            viewAbsolute > nowAbsolute -> expenses
                .sortedBy { timeProvider.dayOfMonth(it.projectedDate) }
                .take(3)
            else -> expenses
                .filter { timeProvider.dayOfMonth(it.projectedDate) >= currentDay }
                .sortedBy { timeProvider.dayOfMonth(it.projectedDate) }
                .take(3)
        }
    }

    private fun calculateCanGoBack(input: MonthPipelineInput): Boolean {

        val currentAbsolute = input.year * 12 + input.month
        val creationMonth = timeProvider.monthValue(input.wallet.createdAt)
        val creationYear = timeProvider.yearValue(input.wallet.createdAt)
        val creationAbsolute = creationYear * 12 + creationMonth

        return currentAbsolute > creationAbsolute
    }
}