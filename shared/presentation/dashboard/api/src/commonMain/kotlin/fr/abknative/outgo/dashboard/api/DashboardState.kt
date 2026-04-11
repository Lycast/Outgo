package fr.abknative.outgo.dashboard.api

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.wallet.api.model.dashboard.ProjectedOperation

/**
 * Represents the complete UI state for the Dashboard screen.
 * Acts as the single source of truth (SSOT) for the view, following UDF patterns.
 *
 * @property isLoading Indicates if a background operation (fetch, sync, or save) is in progress.
 * @property needsOnboarding True if the user has no wallet and should be redirected to the onboarding flow.
 * @property activeWalletId The unique identifier of the currently selected wallet.
 * @property activeWalletName The display name of the active wallet.
 * @property operations The raw list of all projected operations for the selected period.
 * @property filteredOperations The processed list of operations to be rendered, based on [currentFilter] and [isPremium] status.
 * @property currentFilter The active UI filter applied to the operation list.
 * @property currentDay The current physical day of the month (system time).
 * @property currentMonth The current physical month (system time).
 * @property selectedMonth The month currently being viewed or navigated by the user (1-12).
 * @property selectedYear The year currently being viewed or navigated by the user.
 * @property selectedMonthName The localized and formatted string for the selected period (e.g., "April 2026").
 * @property canGoToPreviousMonth UI logic flag to enable/disable the "previous" navigation arrow based on wallet history.
 * @property walletCreationMonth The month the active wallet was created, used as a navigation boundary.
 * @property walletCreationYear The year the active wallet was created, used as a navigation boundary.
 * @property monthlyIncomeInCents The total primary income (budget) for the selected period.
 * @property totalOutgoingsInCents The sum of all planned expenses for the selected period.
 * @property disposableIncomeInCents The remaining balance after subtracting [totalOutgoingsInCents] from [monthlyIncomeInCents].
 * @property remainingToPayInCents The sum of expenses not yet marked as paid or occurring after [currentDay].
 * @property error The current application exception to be handled by the UI (e.g., via Snackbar), or null.
 * @property isHeroExpanded Persisted user preference indicating if the top summary section is visible.
 * @property isPremium Whether the user has access to premium features (influences filtering and UI capabilities).
 */
data class DashboardState(
    val isLoading: Boolean = false,
    val needsOnboarding: Boolean = false,
    val activeWalletId: String? = null,
    val activeWalletName: String = "",

    // --- List Data ---
    val operations: List<ProjectedOperation> = emptyList(),
    val filteredOperations: List<ProjectedOperation> = emptyList(),
    val currentFilter: OperationFilter = OperationFilter.ALL,

    // --- Temporal Logic ---
    val currentDay: Int? = 0,
    val currentMonth: Int = 0,
    val selectedMonth: Int,
    val selectedYear: Int,
    val canGoToPreviousMonth: Boolean = false,
    val walletCreationMonth: Int? = null,
    val walletCreationYear: Int? = null,

    // --- Calculations & Budget ---
    val monthlyIncomeInCents: Long = 0L,
    val totalOutgoingsInCents: Long = 0L,
    val disposableIncomeInCents: Long = 0L,
    val remainingToPayInCents: Long = 0L,

    // --- Global UI State ---
    val error: AppException? = null,
    val isHeroExpanded: Boolean = true,
    val isPremium: Boolean = false
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