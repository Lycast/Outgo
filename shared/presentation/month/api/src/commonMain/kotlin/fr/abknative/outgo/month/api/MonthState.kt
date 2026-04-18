package fr.abknative.outgo.month.api

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.time.EpochMillis
import fr.abknative.outgo.wallet.api.model.operation.Recurrence
import fr.abknative.outgo.wallet.api.model.presenter.ProjectedOperation

data class RecurrenceStat(
    val amountInCents: Long,
    val progress: Float
)

/**
 * Represents the UI state for the Month summary screen.
 * Provides a read-only snapshot of the user's financial health for a specific period.
 */
data class MonthState(
    val isLoading: Boolean = false,
    val activeWalletId: String? = null,
    val incomeOperationId: String? = null,
    val incomeOperationName: String = "Revenu",
    val incomeOperationStartDate: EpochMillis? = null,
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

    val outgoingsProgress: Float = 0f,

    // --- Analyze ---
    val expensesByRecurrence: Map<Recurrence, RecurrenceStat> = emptyMap(),
    val nextUpcomingExpenses: List<ProjectedOperation> = emptyList(),

    // --- Edit state ---
    val isEditWalletDialogVisible: Boolean = false,
    val editWalletNameBuffer: String = "",
    val editWalletAmountBuffer: String = "",

    // --- Global UI State ---
    val error: AppException? = null
) {
    /**
     * Determines if the wallet edit form has valid data and can be submitted.
     * Note: The amount validation (regex) is handled by the Presenter before updating the buffer.
     */
    val isEditWalletFormValid: Boolean
        get() = editWalletNameBuffer.isNotBlank() && editWalletAmountBuffer.isNotBlank()
}