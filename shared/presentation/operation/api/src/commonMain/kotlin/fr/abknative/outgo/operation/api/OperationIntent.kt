package fr.abknative.outgo.operation.api

import fr.abknative.outgo.core.api.time.EpochMillis
import fr.abknative.outgo.wallet.api.model.operation.OperationType
import fr.abknative.outgo.wallet.api.model.operation.Recurrence

/**
 * Represents user actions triggered from the Operation Form UI.
 */
sealed interface OperationIntent {
    /** * Initializes the form. Called when the BottomSheet opens.
     * Passes existing data if editing, or default data if creating.
     */
    data class Init(
        val walletId: String,
        val operationId: String? = null,
        val initialName: String = "",
        val initialAmount: String = "",
        val initialType: OperationType = OperationType.EXPENSE,
        val initialRecurrence: Recurrence = Recurrence.UNIQUE,
        val initialDate: EpochMillis? = null,
        val initialEndDate: EpochMillis? = null
    ) : OperationIntent

    // --- User Inputs ---
    data class UpdateName(val name: String) : OperationIntent
    data class UpdateAmount(val amount: String) : OperationIntent
    data class UpdateType(val type: OperationType) : OperationIntent
    data class UpdateRecurrence(val recurrence: Recurrence) : OperationIntent

    /** * Transforms the currently edited operation into a new creation
     * by clearing its ID and appending a suffix to its name.
     */
    data class Duplicate(val copySuffix: String) : OperationIntent

    /** Called when the user types in the date text field (e.g., "1404") */
    data class UpdateDateInput(val text: String) : OperationIntent

    /** Called when the user selects a date from the visual DatePicker */
    data class SelectDateFromPicker(val millis: EpochMillis) : OperationIntent

    // --- Actions ---
    data object Delete : OperationIntent

    /** Triggers the validation and save process. */
    object Save : OperationIntent

    /** Clears the current error from the UI. */
    object DismissError : OperationIntent
}