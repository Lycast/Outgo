package fr.abknative.outgo.operation.impl

import androidx.lifecycle.viewModelScope
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.core.api.extensions.safeLaunch
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.operation.api.OperationIntent
import fr.abknative.outgo.operation.api.OperationPresenter
import fr.abknative.outgo.operation.api.OperationState
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

    private val _state = MutableStateFlow(OperationState(date = timeProvider.now()))
    override val state: StateFlow<OperationState> = _state.asStateFlow()

    private val onCoroutineError: (AppException) -> Unit = { error ->
        _state.update { it.copy(isSaving = false, error = error) }
    }

    override fun onIntent(intent: OperationIntent) {
        when (intent) {
            is OperationIntent.Init -> handleInit(intent)
            is OperationIntent.UpdateName -> _state.update { it.copy(name = intent.name) }
            is OperationIntent.UpdateAmount -> _state.update { it.copy(amount = intent.amount) }
            is OperationIntent.UpdateType -> _state.update { it.copy(type = intent.type) }
            is OperationIntent.UpdateRecurrence -> _state.update { it.copy(recurrence = intent.recurrence) }
            is OperationIntent.UpdateDate -> _state.update { it.copy(date = intent.date) }
            is OperationIntent.Save -> handleSave()
            is OperationIntent.DismissError -> _state.update { it.copy(error = null) }
        }
    }

    private fun handleInit(intent: OperationIntent.Init) {
        _state.update {
            it.copy(
                walletId = intent.walletId,
                operationId = intent.operationId,
                name = intent.initialName,
                amount = intent.initialAmount,
                type = intent.initialType,
                recurrence = intent.initialRecurrence,
                date = intent.initialDate ?: timeProvider.now(),
                isSavedSuccessfully = false // Reset success flag on open
            )
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