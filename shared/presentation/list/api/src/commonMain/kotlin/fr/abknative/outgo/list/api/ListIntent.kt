package fr.abknative.outgo.list.api

import fr.abknative.outgo.wallet.api.model.presenter.ProjectedOperation

/**
 * Represents all possible user actions (intents) triggered from the List UI.
 * Follows the Unidirectional Data Flow (UDF) architecture.
 */
sealed interface ListIntent {

    /** * Intent to soft-delete an operation by its [id].
     */
    data class Delete(val id: String) : ListIntent

    /** * Intent to end a subscription by setting its end date to today.
     * Reuses the save use case to update the existing operation in the database.
     */
    data class EndSubscription(val projectedOp: ProjectedOperation) : ListIntent

    // --- UI Navigation & Filtering ---
    /** * Intent to change the currently displayed month.
     */
    data class NavigateMonth(val isNext: Boolean) : ListIntent

    /** Switches between the Projected (Month) and Standard (Rules) views. */
    data class SwitchViewMode(val mode: ListViewMode) : ListIntent

    /** Updates the filter chip when in Projected Mode. */
    data class UpdateProjectedFilter(val filter: ProjectedFilter) : ListIntent

    /** Updates the filter chip when in Standard Mode. */
    data class UpdateStandardFilter(val filter: StandardFilter) : ListIntent

    // --- Error Handling ---
    /** * Intent to clear the current error state from the UI.
     */
    object DismissError : ListIntent
}