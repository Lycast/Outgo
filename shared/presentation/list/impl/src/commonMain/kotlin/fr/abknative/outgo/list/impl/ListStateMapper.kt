package fr.abknative.outgo.list.impl

import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.list.api.ListState
import fr.abknative.outgo.list.api.OperationFilter
import fr.abknative.outgo.wallet.api.model.operation.OperationType
import fr.abknative.outgo.wallet.api.model.presenter.PeriodStats
import fr.abknative.outgo.wallet.api.model.presenter.ProjectedOperation

/**
 * Pure logic component responsible for mapping domain data and UI parameters
 * into a final [fr.abknative.outgo.list.api.ListState].
 */
internal class ListStateMapper(private val timeProvider: TimeProvider) {

    /**
     * Maps raw inputs into a complete UI state.
     */
    fun mapToState(
        currentOperations: List<ProjectedOperation>,
        stats: PeriodStats,
        input: PipelineInput, // A small helper data class
        currentHeroExpanded: Boolean
    ): ListState {
        return ListState(
            isLoading = false,
            activeWalletId = input.wallet.id,
            activeWalletName = input.wallet.name,
            walletCreationMonth = timeProvider.monthValue(input.wallet.createdAt),
            walletCreationYear = timeProvider.yearValue(input.wallet.createdAt),
            operations = currentOperations,
            filteredOperations = filterOperations(
                ops = currentOperations,
                filter = input.filter,
                isPremium = input.isPremium,
                viewMonth = input.month,
                viewYear = input.year
            ),
            currentFilter = input.filter,
            selectedMonth = input.month,
            selectedYear = input.year,
            canGoToPreviousMonth = calculateCanGoBack(input),
            monthlyIncomeInCents = currentOperations
                .filter { it.operation.type == OperationType.INCOME }
                .sumOf { it.operation.amountInCents },
            totalOutgoingsInCents = stats.totalExpensesInCents,
            remainingToPayInCents = stats.remainingToPayInCents,
            disposableIncomeInCents = stats.disposableIncomeInCents,
            isHeroExpanded = currentHeroExpanded,
            isPremium = input.isPremium,
            currentDay = timeProvider.dayOfMonth(),
            currentMonth = timeProvider.monthValue()
        )
    }

    private fun filterOperations(
        ops: List<ProjectedOperation>,
        filter: OperationFilter,
        isPremium: Boolean,
        viewMonth: Int,
        viewYear: Int
    ): List<ProjectedOperation> {
        val baseList = if (isPremium) ops else ops.filter { it.operation.type == OperationType.EXPENSE }

        val currentDay = timeProvider.dayOfMonth()
        val nowMonth = timeProvider.monthValue()
        val nowYear = timeProvider.yearValue()

        val viewAbsolute = viewYear * 12 + viewMonth
        val nowAbsolute = nowYear * 12 + nowMonth

        return when (filter) {
            OperationFilter.ALL -> baseList
            OperationFilter.PAST -> when {
                viewAbsolute < nowAbsolute -> baseList
                viewAbsolute > nowAbsolute -> emptyList()
                else -> baseList.filter { timeProvider.dayOfMonth(it.projectedDate) < currentDay }
            }
            OperationFilter.REMAINING -> when {
                viewAbsolute < nowAbsolute -> emptyList()
                viewAbsolute > nowAbsolute -> baseList
                else -> baseList.filter { timeProvider.dayOfMonth(it.projectedDate) >= currentDay }
            }
        }
    }

    private fun calculateCanGoBack(input: PipelineInput): Boolean {
        val currentAbsolute = input.year * 12 + input.month
        val creationMonth = timeProvider.monthValue(input.wallet.createdAt)
        val creationYear = timeProvider.yearValue(input.wallet.createdAt)
        val creationAbsolute = creationYear * 12 + creationMonth
        return currentAbsolute > creationAbsolute
    }
}