package fr.abknative.outgo.month.api

import fr.abknative.outgo.core.api.EpochMillis

/**
 * Represents user actions triggered from the Month summary screen.
 */
sealed interface MonthIntent {

    data class SaveWalletAndIncome(
        val walletId: String,
        val walletName: String,
        val incomeAmountInCents: Long,
        val incomeOperationId: String?,
        val incomeOperationName: String,
        val startDate: EpochMillis
    ) : MonthIntent

    /** Intent to rename the current wallet. */
    data class RenameWallet(val id: String?, val newName: String) : MonthIntent

    /** Intent to change the currently viewed month. */
    data class NavigateMonth(val isNext: Boolean) : MonthIntent

    /** Intent to clear the current error state from the UI. */
    data object DismissError : MonthIntent
}