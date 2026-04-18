package fr.abknative.outgo.operation.api

import fr.abknative.outgo.core.api.EpochMillis
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.wallet.api.model.operation.OperationType
import fr.abknative.outgo.wallet.api.model.operation.Recurrence

/**
 * Represents the UI state for the Operation Form.
 * Handles both creation of a new operation and edition of an existing one.
 */
data class OperationState(
    val operationId: String? = null,
    val walletId: String = "",
    val name: String = "",
    val amount: String = "",
    val type: OperationType = OperationType.EXPENSE,
    val recurrence: Recurrence = Recurrence.UNIQUE,

    val startDate: EpochMillis = 0L,
    val endDate: EpochMillis? = null,

    val dateInputBuffer: String = "",
    val isDateError: Boolean = false,

    // Form Status
    val isSaving: Boolean = false,
    val isSavedSuccessfully: Boolean = false,
    val error: AppException? = null
) {
    /**
     * Determines if the form has valid data and can be submitted.
     * Note: Since the Presenter will block invalid amount strings via the AmountValidator,
     * we just need to check if it's not blank.
     */
    val isFormValid: Boolean
        get() = name.isNotBlank()
                && amount.isNotBlank()
                && !isDateError
                && dateInputBuffer.length == 8
}