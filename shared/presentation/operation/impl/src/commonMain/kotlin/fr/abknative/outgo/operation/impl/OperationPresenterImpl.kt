package fr.abknative.outgo.operation.impl

import androidx.lifecycle.viewModelScope
import fr.abknative.outgo.core.api.extensions.safeLaunch
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.core.api.time.EpochMillis
import fr.abknative.outgo.core.api.time.TimeProvider
import fr.abknative.outgo.core.api.validators.AmountValidator
import fr.abknative.outgo.core.api.validators.DateValidator
import fr.abknative.outgo.core.api.validators.NameValidator
import fr.abknative.outgo.operation.api.OperationIntent
import fr.abknative.outgo.operation.api.OperationPresenter
import fr.abknative.outgo.operation.api.OperationState
import fr.abknative.outgo.wallet.api.model.operation.Recurrence
import fr.abknative.outgo.wallet.api.usecase.SaveOperationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.roundToLong

internal class OperationPresenterImpl(
    private val saveOperation: SaveOperationUseCase,
    private val timeProvider: TimeProvider
) : OperationPresenter() {

    private val dateValidator = DateValidator(timeProvider)

    private val _state = MutableStateFlow(OperationState(startDate = timeProvider.now()))
    override val state: StateFlow<OperationState> = _state.asStateFlow()

    private val onCoroutineError: (AppException) -> Unit = { error ->
        _state.update { it.copy(isSaving = false, error = error) }
    }

    override fun onIntent(intent: OperationIntent) {
        when (intent) {
            is OperationIntent.Init -> handleInit(intent)
            is OperationIntent.UpdateName -> {
                val validName = NameValidator.validate(intent.name)
                _state.update { it.copy(name = validName) }
            }
            is OperationIntent.UpdateAmount -> {
                AmountValidator.validate(intent.amount)?.let { validAmount ->
                    _state.update { it.copy(amount = validAmount) }
                }
            }
            is OperationIntent.UpdateStartDateInput -> handleStartDateInput(intent.text)
            is OperationIntent.SelectStartDateFromPicker -> handleStartDateSelection(intent.millis)
            is OperationIntent.UpdateEndDateInput -> handleEndDateInput(intent.text)
            is OperationIntent.SelectEndDateFromPicker -> handleEndDateSelection(intent.millis)
            is OperationIntent.ClearEndDate -> handleClearEndDate()
            is OperationIntent.UpdateType -> _state.update { it.copy(type = intent.type) }
            is OperationIntent.UpdateRecurrence -> _state.update { it.copy(recurrence = intent.recurrence) }
            is OperationIntent.Save -> handleSave()
            is OperationIntent.DismissError -> _state.update { it.copy(error = null) }
        }
    }

    private fun handleInit(intent: OperationIntent.Init) {
        val initialStartDate = intent.initialStartDate ?: timeProvider.now()
        val initialDateBuffer = dateValidator.formatMillis(initialStartDate)
        val initialEndDateBuffer = intent.initialEndDate?.let { dateValidator.formatMillis(it) } ?: ""

        _state.update {
            it.copy(
                walletId = intent.walletId,
                operationId = intent.operationId,
                name = intent.initialName,
                amount = intent.initialAmount,
                type = intent.initialType,
                recurrence = intent.initialRecurrence,
                startDate = initialStartDate,
                endDate = intent.initialEndDate,
                startDateInputBuffer = initialDateBuffer,
                endDateInputBuffer = initialEndDateBuffer,
                isStartDateError = false,
                isEndDateError = false,
                isSavedSuccessfully = false
            )
        }
    }

    private fun handleStartDateInput(newText: String) {
        if (newText.length > 8 || !newText.all { it.isDigit() }) return
        val isPartialValid = dateValidator.isPartialInputValid(newText)
        _state.update { currentState ->
            var updatedState = currentState.copy(
                startDateInputBuffer = newText,
                isStartDateError = !isPartialValid
            )
            if (newText.length == 8 && isPartialValid) {
                val newMillis = dateValidator.deriveMillis(newText)
                updatedState = updatedState.copy(startDate = newMillis)
            }
            updatedState
        }
    }

    private fun handleStartDateSelection(millis: EpochMillis) {
        val formattedString = dateValidator.formatMillis(millis)
        _state.update {
            it.copy(
                startDate = millis,
                startDateInputBuffer = formattedString,
                isStartDateError = false
            )
        }
    }

    private fun handleEndDateInput(newText: String) {
        if (newText.length > 8 || !newText.all { it.isDigit() }) return
        val isPartialValid = newText.isEmpty() || dateValidator.isPartialInputValid(newText)
        _state.update { currentState ->
            var updatedState = currentState.copy(
                endDateInputBuffer = newText,
                isEndDateError = !isPartialValid
            )
            if (newText.length == 8 && isPartialValid) {
                updatedState = updatedState.copy(endDate = dateValidator.deriveMillis(newText))
            } else if (newText.isEmpty()) {
                updatedState = updatedState.copy(endDate = null)
            }
            updatedState
        }
    }

    private fun handleEndDateSelection(millis: EpochMillis) {
        _state.update {
            it.copy(
                endDate = millis,
                endDateInputBuffer = dateValidator.formatMillis(millis),
                isEndDateError = false
            )
        }
    }

    private fun handleClearEndDate() {
        _state.update {
            it.copy(
                endDate = null,
                endDateInputBuffer = "",
                isEndDateError = false
            )
        }
    }

    private fun handleSave() {
        val currentState = _state.value
        if (!currentState.isFormValid) return

        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isSaving = true, error = null) }

            val amountInCents = currentState.amount.toCents()
            val sanitizedEndDate = if (currentState.recurrence == Recurrence.UNIQUE) null else currentState.endDate

            val result = saveOperation(
                id = currentState.operationId,
                walletId = currentState.walletId,
                name = currentState.name.trim(),
                amountInCents = amountInCents,
                type = currentState.type,
                recurrence = currentState.recurrence,
                startDate = currentState.startDate,
                endDate = sanitizedEndDate
            )

            when (result) {
                is Result.Success -> {
                    _state.update { it.copy(isSaving = false, isSavedSuccessfully = true) }
                }
                is Result.Error -> {
                    _state.update { it.copy(isSaving = false, error = result.error) }
                }
            }
        }
    }

    /**
     * Converts a raw string input (like "12.50" or "12,5") into cents format.
     */
    private fun String.toCents(): Long {
        val sanitized = this.replace(",", ".")
        val doubleValue = sanitized.toDoubleOrNull() ?: 0.0
        return (doubleValue * 100).roundToLong()
    }
}