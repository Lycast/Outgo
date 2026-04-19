package fr.abknative.outgo.operation.api

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.time.EpochMillis
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

    val startDateInputBuffer: String = "",
    val isStartDateError: Boolean = false,
    val endDateInputBuffer: String = "",
    val isEndDateError: Boolean = false,

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
        get() {
            val isBaseValid = name.isNotBlank() && amount.isNotBlank() && !isStartDateError && startDateInputBuffer.length == 8

            val isEndDateValid = recurrence == Recurrence.UNIQUE ||
                    (!isEndDateError && (endDateInputBuffer.isEmpty() || endDateInputBuffer.length == 8))

            val isDateOrderValid = endDate == null || startDate <= endDate

            return isBaseValid && isEndDateValid && isDateOrderValid
        }
}