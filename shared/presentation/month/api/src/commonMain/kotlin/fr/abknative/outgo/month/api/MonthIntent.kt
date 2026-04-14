package fr.abknative.outgo.month.api

/**
 * Represents user actions triggered from the Month summary screen.
 */
sealed interface MonthIntent {

    /** Intent to rename the current wallet. */
    data class RenameWallet(val id: String?, val newName: String) : MonthIntent

    /** Intent to change the currently viewed month. */
    data class NavigateMonth(val isNext: Boolean) : MonthIntent

    /** Intent to clear the current error state from the UI. */
    data object DismissError : MonthIntent
}