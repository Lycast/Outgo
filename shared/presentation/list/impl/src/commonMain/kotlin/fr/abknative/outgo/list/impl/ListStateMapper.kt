package fr.abknative.outgo.list.impl

import fr.abknative.outgo.core.api.time.DateTimeFormatter
import fr.abknative.outgo.core.api.time.TimeProvider
import fr.abknative.outgo.list.api.ListState
import fr.abknative.outgo.list.api.ListViewMode
import fr.abknative.outgo.list.api.ProjectedFilter
import fr.abknative.outgo.list.api.StandardFilter
import fr.abknative.outgo.wallet.api.model.operation.OperationType
import fr.abknative.outgo.wallet.api.model.operation.Recurrence
import fr.abknative.outgo.wallet.api.model.presenter.ProjectedOperation

internal class ListStateMapper(
    private val dateTimeFormatter: DateTimeFormatter,
    private val timeProvider: TimeProvider
) {

    fun mapToState(
        currentOperations: List<ProjectedOperation>,
        input: PipelineInput,
    ): ListState {


        val baseOperations = if (input.isPremium) { currentOperations } else {
            currentOperations.filter { it.operation.type == OperationType.EXPENSE } }

        val filteredOperations = when (input.viewMode) {
            ListViewMode.PROJECTED -> applyProjectedFilter(baseOperations, input)
            ListViewMode.STANDARD -> applyStandardFilter(baseOperations, input.standardFilter)
        }

        val grouped = groupOperations(filteredOperations, input.viewMode)

        return ListState(
            isLoading = false,
            activeWalletId = input.wallet.id,
            activeWalletName = input.wallet.name,
            walletCreationMonth = timeProvider.monthValue(input.wallet.createdAt),
            walletCreationYear = timeProvider.yearValue(input.wallet.createdAt),

            viewMode = input.viewMode,
            projectedFilter = input.projectedFilter,
            standardFilter = input.standardFilter,

            operations = currentOperations,
            groupedOperations = grouped,

            selectedMonth = input.month,
            selectedYear = input.year,
            canGoToPreviousMonth = calculateCanGoBack(input),
            currentDay = timeProvider.dayOfMonth(),
            currentMonth = timeProvider.monthValue(),
            isPremium = input.isPremium
        )
    }

    private fun applyProjectedFilter(
        ops: List<ProjectedOperation>,
        input: PipelineInput
    ): List<ProjectedOperation> {
        val filter = input.projectedFilter
        if (filter == ProjectedFilter.ALL) return ops

        val currentDay = timeProvider.dayOfMonth()
        val nowMonth = timeProvider.monthValue()
        val nowYear = timeProvider.yearValue()

        val viewAbsolute = input.year * 12 + input.month
        val nowAbsolute = nowYear * 12 + nowMonth

        return when (filter) {
            ProjectedFilter.PAST -> when {
                viewAbsolute < nowAbsolute -> ops
                viewAbsolute > nowAbsolute -> emptyList()
                else -> ops.filter { timeProvider.dayOfMonth(it.projectedDate) < currentDay }
            }
            ProjectedFilter.REMAINING -> when {
                viewAbsolute < nowAbsolute -> emptyList()
                viewAbsolute > nowAbsolute -> ops
                else -> ops.filter { timeProvider.dayOfMonth(it.projectedDate) >= currentDay }
            }
            ProjectedFilter.ALL -> ops
        }
    }

    private fun applyStandardFilter(
        ops: List<ProjectedOperation>,
        filter: StandardFilter
    ): List<ProjectedOperation> {
        if (filter == StandardFilter.ALL) return ops

        return ops.filter { projected ->
            val recurrence = projected.operation.recurrence
            when (filter) {
                StandardFilter.UNIQUE -> recurrence == Recurrence.UNIQUE
                StandardFilter.WEEKLY -> recurrence == Recurrence.WEEKLY
                StandardFilter.MONTHLY -> recurrence == Recurrence.MONTHLY
                StandardFilter.YEARLY -> recurrence == Recurrence.YEARLY
                StandardFilter.ALL -> true
            }
        }
    }


    private fun groupOperations(
        ops: List<ProjectedOperation>,
        mode: ListViewMode
    ): Map<String, List<ProjectedOperation>> {
        if (ops.isEmpty()) return emptyMap()

        return when (mode) {
            ListViewMode.PROJECTED -> {
                ops.groupBy { projectedOp ->
                    dateTimeFormatter.formatLongDate(projectedOp.projectedDate)
                }
            }
            ListViewMode.STANDARD -> {
                mapOf("GLOBAL_RULES" to ops)
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