package fr.abknative.outgo.month.api

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.wallet.api.model.operation.Recurrence
import fr.abknative.outgo.wallet.api.model.presenter.ProjectedOperation

/**
 * Represents the UI state for the Month summary screen.
 * Provides a read-only snapshot of the user's financial health for a specific period.
 */
data class MonthState(
    val isLoading: Boolean = false,
    val activeWalletName: String = "",

    // --- Temporal Logic ---
    val selectedMonth: Int = 1,
    val selectedYear: Int = 2026,
    val canGoToPreviousMonth: Boolean = false,

    // --- Calculations & Budget ---
    val monthlyIncomeInCents: Long = 0L,
    val totalOutgoingsInCents: Long = 0L,
    val disposableIncomeInCents: Long = 0L,
    val remainingToPayInCents: Long = 0L,

    // Analyse détaillée
    val expensesByRecurrence: Map<Recurrence, Long> = emptyMap(),
    val nextUpcomingExpenses: List<ProjectedOperation> = emptyList(),

    // --- Global UI State ---
    val error: AppException? = null
)

/**
 * Defines the available filtering strategies for the dashboard operation list.
 */
enum class OperationFilter {
    /** Show all operations for the selected period regardless of their date. */
    ALL,
    /** Show only operations that occurred before the current day. */
    PAST,
    /** Show only operations that are yet to occur or are scheduled for today. */
    REMAINING
}