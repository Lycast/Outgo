package fr.abknative.outgo.list.api

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.wallet.api.model.presenter.ProjectedOperation

/**
 * Represents the complete UI state for the List screen.
 * Acts as the single source of truth (SSOT) for the view, following UDF patterns.
 */
data class ListState(
    val isLoading: Boolean = false,
    val activeWalletId: String? = null,
    val activeWalletName: String = "",

    // --- UI Modes & Filters ---
    val viewMode: ListViewMode = ListViewMode.PROJECTED,
    val projectedFilter: ProjectedFilter = ProjectedFilter.REMAINING,
    val standardFilter: StandardFilter = StandardFilter.ALL,

    // --- List Data ---
    /** The raw operations fetched from the UseCase (used for math/stats). */
    val operations: List<ProjectedOperation> = emptyList(),

    /** * The processed and grouped list of operations to be rendered by Compose.
     * Key = The sticky header title (e.g., "Today", "Monthly")
     * Value = The operations falling under this category.
     */
    val groupedOperations: Map<String, List<ProjectedOperation>> = emptyMap(),

    // --- Temporal Logic ---
    val currentDay: Int? = 0,
    val currentMonth: Int = 0,
    val selectedMonth: Int,
    val selectedYear: Int,
    val canGoToPreviousMonth: Boolean = false,
    val walletCreationMonth: Int? = null,
    val walletCreationYear: Int? = null,

    // --- Global UI State ---
    val error: AppException? = null,
    val isPremium: Boolean = false
)