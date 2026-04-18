package fr.abknative.outgo.wallet.impl.usecase

import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.wallet.api.model.Operation
import fr.abknative.outgo.wallet.api.model.operation.Recurrence
import fr.abknative.outgo.wallet.api.model.presenter.ProjectedOperation
import fr.abknative.outgo.wallet.api.repository.OperationRepository
import fr.abknative.outgo.wallet.api.usecase.ObserveProjectedOperationsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ObserveProjectedOperationsUseCaseImpl(
    private val repository: OperationRepository,
    private val timeProvider: TimeProvider
) : ObserveProjectedOperationsUseCase {

    //
    override fun invoke(walletId: String, month: Int, year: Int): Flow<List<ProjectedOperation>> {
        val startOfMonth = timeProvider.startOfMonth(month, year)
        val endOfMonth = timeProvider.endOfMonth(month, year)

        return repository.observeOperationsByPeriod(walletId, startOfMonth, endOfMonth).map { operations ->
            operations
                .flatMap { op ->
                    projectOperation(op, month, year, startOfMonth, endOfMonth)
                }
                .sortedBy { projectedOp -> projectedOp.projectedDate }
        }
    }

    /**
     * Projects a single operation rule into one or more virtual occurrences for the given month.
     * * @param op The original operation rule from the database.
     * @param targetMonth The month currently displayed in the UI.
     * @param targetYear The year currently displayed in the UI.
     * @param targetStartOfMonth The timestamp of the first day of the target month.
     * @param targetEndOfMonth The timestamp of the last day of the target month.
     * @return A list of projected operations to display.
     */
    private fun projectOperation(
        op: Operation,
        targetMonth: Int,
        targetYear: Int,
        targetStartOfMonth: Long,
        targetEndOfMonth: Long
    ): List<ProjectedOperation> {
        val opMonth = timeProvider.monthValue(op.startDate)
        val opYear = timeProvider.yearValue(op.startDate)
        val opDay = timeProvider.dayOfMonth(op.startDate)

        return when (op.recurrence) {
            Recurrence.UNIQUE -> {
                if (opMonth == targetMonth && opYear == targetYear) {
                    listOf(ProjectedOperation(operation = op, projectedDate = op.startDate, formattedDate = timeProvider.formatShortDate(op.startDate)))
                } else emptyList()
            }

            Recurrence.MONTHLY -> {
                projectSingleOccurrence(op, opDay, targetStartOfMonth)
            }

            Recurrence.WEEKLY -> {
                generateWeeklyOccurrences(op, targetStartOfMonth, targetEndOfMonth)
            }

            Recurrence.YEARLY -> {
                if (opMonth == targetMonth) {
                    projectSingleOccurrence(op, opDay, targetStartOfMonth)
                } else emptyList()
            }

            else -> emptyList()
        }
    }

    /**
     * Calculates all weekly occurrences of an operation that fall within the target month.
     */
    private fun generateWeeklyOccurrences(
        op: Operation,
        targetStartOfMonth: Long,
        targetEndOfMonth: Long
    ): List<ProjectedOperation> {
        val occurrences = mutableListOf<ProjectedOperation>()

        var currentPointer = op.startDate

        while (currentPointer < targetStartOfMonth) {
            currentPointer = timeProvider.plusDays(currentPointer, 7)
        }

        val safeEndDate = op.endDate
        val absoluteEndLimit = if (safeEndDate != null) {
            minOf(targetEndOfMonth, safeEndDate)
        } else {
            targetEndOfMonth
        }

        while (currentPointer <= absoluteEndLimit) {
            val finalDate = timeProvider.combineDateAndTime(currentPointer, 0, 0)

            occurrences.add(
                ProjectedOperation(
                    operation = op,
                    projectedDate = finalDate,
                    formattedDate = timeProvider.formatShortDate(finalDate)
                )
            )

            currentPointer = timeProvider.plusDays(currentPointer, 7)
        }

        return occurrences
    }

    /**
     * Helper method to project a single monthly or yearly occurrence.
     * Safely clamps the original operation day to the target month's maximum days
     * to prevent date overflow (e.g., mapping Jan 31st to Feb 28th).
     *
     * @param op The original operation rule.
     * @param opDay The original day of the month the operation was set to.
     * @param targetStartOfMonth The timestamp representing the first day of the currently viewed month.
     * @return A list containing the single wrapped [ProjectedOperation].
     */
    private fun projectSingleOccurrence(
        op: Operation,
        opDay: Int,
        targetStartOfMonth: Long
    ): List<ProjectedOperation> {
        val maxDays = timeProvider.lastDayOfMonth(targetStartOfMonth)
        val safeDay = opDay.coerceAtMost(maxDays)
        val projected = timeProvider.plusDays(targetStartOfMonth, safeDay - 1)
        val finalDate = timeProvider.combineDateAndTime(projected, 0, 0)

        return listOf(ProjectedOperation(operation = op, projectedDate = finalDate, formattedDate = timeProvider.formatShortDate(finalDate)))
    }
}
