package fr.abknative.outgo.outgoing.api.presenter

import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.outgoing.api.model.Outgoing

/**
 * Represents the complete UI state for the Outgoing screen.
 * Acts as the single source of truth for the view.
 *
 * @property isLoading Indicates if a background operation (fetch, sync) is in progress.
 * @property outgoings The list of expenses to display for the currently selected period.
 * @property currentDay The current physical day of the month.
 * @property currentMonth The current physical month.
 * @property selectedMonth The month currently being viewed or navigated to by the user.
 * @property monthlyIncomeInCents The user's total base income or overall budget.
 * @property totalOutgoingsInCents The smoothed sum of all expenses (e.g., accounting for yearly recurrences).
 * @property disposableIncomeInCents The remaining budget after subtracting [totalOutgoingsInCents] from income.
 * @property remainingToPayInCents The exact sum of expenses still due in the [selectedMonth] based on the [currentDay].
 * @property isCloudSyncActive Indicates if the app is actively synchronizing data with the remote server.
 * @property error The current error state to be handled by the UI, or null if successful.
 * @property isHeroExpanded Persisted user preference indicating if the top summary section is expanded.
 */
data class OutgoingState(
    val isLoading: Boolean = false,

    // --- List Data ---
    val outgoings: List<Outgoing> = emptyList(),
    val currentDay: Int? = 0,
    val currentMonth: Int = 0,
    val selectedMonth: Int,

    // --- Calculations & Budget ---
    val monthlyIncomeInCents: Long = 0L,
    val totalOutgoingsInCents: Long = 0L,
    val disposableIncomeInCents: Long = 0L,
    val remainingToPayInCents: Long = 0L,

    // --- Global State ---
    val isCloudSyncActive: Boolean = false,
    val error: AppException? = null,

    // --- KeyValueStorage ---
    val isHeroExpanded: Boolean = true
)