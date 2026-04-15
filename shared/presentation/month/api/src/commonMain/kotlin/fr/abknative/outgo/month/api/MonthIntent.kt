package fr.abknative.outgo.month.api

/**
 * Represents user actions triggered from the Month summary screen.
 */
sealed interface MonthIntent {

    // --- Actions For Premium ---
    /** Intent to strictly rename the current wallet (Premium behavior). */
    data class RenameWallet(val id: String?, val newName: String) : MonthIntent

    // --- Modale d'édition (Gratuit / Mixte) ---
    /** Requests opening the wallet edit dialog. */
    object OpenEditWalletDialog : MonthIntent

    /** Requests closing the wallet edit dialog without saving. */
    object CloseEditWalletDialog : MonthIntent

    /** Triggered when the user types a new wallet name. */
    data class UpdateEditWalletName(val name: String) : MonthIntent

    /** Triggered when the user types a new income amount. */
    data class UpdateEditWalletAmount(val amount: String) : MonthIntent

    /** Submits the wallet and income modifications using the current state buffers. */
    object SubmitWalletAndIncome : MonthIntent

    /** Intent to change the currently viewed month. */
    data class NavigateMonth(val isNext: Boolean) : MonthIntent

    /** Intent to clear the current error state from the UI. */
    object DismissError : MonthIntent
}