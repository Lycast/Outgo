package fr.abknative.outgo.list.api

/**
 * Represents all possible user actions (intents) triggered from the List UI.
 * Follows the Unidirectional Data Flow (UDF) architecture.
 */
sealed interface ListIntent {

    /** * Intent to soft-delete an operation by its [id].
     */
    data class Delete(val id: String) : ListIntent

    // --- UI Navigation & Filtering ---
    /** * Intent to change the currently displayed month.
     */
    data class NavigateMonth(val isNext: Boolean) : ListIntent

    /** * Intent to change the active UI filter for the operation list.
     */
    data class UpdateFilter(val filter: OperationFilter) : ListIntent

    // --- Error Handling ---
    /** * Intent to clear the current error state from the UI.
     */
    object DismissError : ListIntent
}