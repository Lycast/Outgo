package fr.abknative.outgo.operation.impl

import androidx.lifecycle.viewModelScope
import fr.abknative.outgo.core.api.EpochMillis
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.core.api.extensions.safeLaunch
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.core.api.validators.AmountValidator
import fr.abknative.outgo.core.api.validators.DateValidator
import fr.abknative.outgo.core.api.validators.NameValidator
import fr.abknative.outgo.operation.api.OperationIntent
import fr.abknative.outgo.operation.api.OperationPresenter
import fr.abknative.outgo.operation.api.OperationState
import fr.abknative.outgo.wallet.api.usecase.DeleteOperationUseCase
import fr.abknative.outgo.wallet.api.usecase.SaveOperationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.roundToLong

internal class OperationPresenterImpl(
    private val saveOperation: SaveOperationUseCase,
    private val deleteOperation: DeleteOperationUseCase,
    private val timeProvider: TimeProvider
) : OperationPresenter() {

    private val dateValidator = DateValidator(timeProvider)

    private val _state = MutableStateFlow(OperationState(date = timeProvider.now()))
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
            is OperationIntent.UpdateDateInput -> handleDateInput(intent.text)
            is OperationIntent.SelectDateFromPicker -> handleDateSelection(intent.millis)
            is OperationIntent.UpdateType -> _state.update { it.copy(type = intent.type) }
            is OperationIntent.UpdateRecurrence -> _state.update { it.copy(recurrence = intent.recurrence) }
            is OperationIntent.Delete -> handleDelete()
            is OperationIntent.Save -> handleSave()
            is OperationIntent.DismissError -> _state.update { it.copy(error = null) }
        }
    }

    private fun handleInit(intent: OperationIntent.Init) {
        val initialDate = intent.initialDate ?: timeProvider.now()
        val initialDateBuffer = dateValidator.formatMillis(initialDate)

        _state.update {
            it.copy(
                walletId = intent.walletId,
                operationId = intent.operationId,
                name = intent.initialName,
                amount = intent.initialAmount,
                type = intent.initialType,
                recurrence = intent.initialRecurrence,
                date = initialDate,
                dateInputBuffer = initialDateBuffer,
                isDateError = false,
                isSavedSuccessfully = false
            )
        }
    }

    private fun handleDateInput(newText: String) {
        if (newText.length > 8 || !newText.all { it.isDigit() }) return

        val isPartialValid = dateValidator.isPartialInputValid(newText)

        _state.update { currentState ->
            var updatedState = currentState.copy(
                dateInputBuffer = newText,
                isDateError = !isPartialValid
            )

            if (newText.length == 8 && isPartialValid) {
                val newMillis = dateValidator.deriveMillis(newText)
                updatedState = updatedState.copy(date = newMillis)
            }

            updatedState
        }
    }

    private fun handleDateSelection(millis: EpochMillis) {
        val formattedString = dateValidator.formatMillis(millis)
        _state.update {
            it.copy(
                date = millis,
                dateInputBuffer = formattedString,
                isDateError = false
            )
        }
    }

    private fun handleDelete() {
        val id = _state.value.operationId ?: return

        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isSaving = true) }
            val result = deleteOperation(id)

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

    private fun handleSave() {
        val currentState = _state.value
        if (!currentState.isFormValid) return

        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isSaving = true, error = null) }

            val amountInCents = currentState.amount.toCents()

            val result = saveOperation(
                id = currentState.operationId,
                walletId = currentState.walletId,
                name = currentState.name.trim(),
                amountInCents = amountInCents,
                type = currentState.type,
                recurrence = currentState.recurrence,
                startDate = currentState.date,
                endDate = null
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