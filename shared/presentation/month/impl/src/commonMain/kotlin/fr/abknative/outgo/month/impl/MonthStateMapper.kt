package fr.abknative.outgo.month.impl

import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.month.api.MonthState
import fr.abknative.outgo.wallet.api.model.operation.OperationType
import fr.abknative.outgo.wallet.api.model.presenter.PeriodStats
import fr.abknative.outgo.wallet.api.model.presenter.ProjectedOperation

internal class MonthStateMapper(private val timeProvider: TimeProvider) {

    fun mapToState(
        currentOperations: List<ProjectedOperation>,
        stats: PeriodStats,
        input: MonthPipelineInput
    ): MonthState {

        val expensesOnly = currentOperations.filter { it.operation.type == OperationType.EXPENSE }

        return MonthState(
            isLoading = false,
            activeWalletName = input.wallet.name,
            selectedMonth = input.month,
            selectedYear = input.year,
            canGoToPreviousMonth = calculateCanGoBack(input),
            monthlyIncomeInCents = currentOperations
                .filter { it.operation.type == OperationType.INCOME }
                .sumOf { it.operation.amountInCents },
            totalOutgoingsInCents = stats.totalExpensesInCents,
            remainingToPayInCents = stats.remainingToPayInCents,
            disposableIncomeInCents = stats.disposableIncomeInCents,

            // 🌟 1. Calcul des totaux par récurrence
            expensesByRecurrence = expensesOnly
                .groupBy { it.operation.recurrence }
                .mapValues { (_, ops) -> ops.sumOf { it.operation.amountInCents } },

            // 🌟 2. Récupération des 3 prochaines dépenses
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
            // Si on regarde un mois passé : Il n'y a pas de "prochaines" dépenses
            viewAbsolute < nowAbsolute -> emptyList()

            // Si on regarde un mois futur : On prend les 3 premières dépenses du mois
            viewAbsolute > nowAbsolute -> expenses
                .sortedBy { timeProvider.dayOfMonth(it.projectedDate) }
                .take(3)

            // Si on regarde le mois actuel : On prend les 3 prochaines à partir d'aujourd'hui
            else -> expenses
                .filter { timeProvider.dayOfMonth(it.projectedDate) >= currentDay }
                .sortedBy { timeProvider.dayOfMonth(it.projectedDate) }
                .take(3)
        }
    }

    private fun calculateCanGoBack(input: MonthPipelineInput): Boolean {
        if (!input.isPremium) return false

        val currentAbsolute = input.year * 12 + input.month
        val creationMonth = timeProvider.monthValue(input.wallet.createdAt)
        val creationYear = timeProvider.yearValue(input.wallet.createdAt)
        val creationAbsolute = creationYear * 12 + creationMonth

        return currentAbsolute > creationAbsolute
    }
}