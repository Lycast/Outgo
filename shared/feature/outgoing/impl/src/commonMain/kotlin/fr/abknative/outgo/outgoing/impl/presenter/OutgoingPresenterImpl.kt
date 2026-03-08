package fr.abknative.outgo.outgoing.impl.presenter

import androidx.lifecycle.viewModelScope
import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.core.api.Result
import fr.abknative.outgo.core.api.extensions.safeLaunch
import fr.abknative.outgo.outgoing.api.presenter.OutgoingIntent
import fr.abknative.outgo.outgoing.api.presenter.OutgoingPresenter
import fr.abknative.outgo.outgoing.api.presenter.OutgoingState
import fr.abknative.outgo.outgoing.api.usecase.*
import kotlinx.coroutines.flow.*

internal class OutgoingPresenterImpl(
    private val observeActiveOutgoings: ObserveActiveOutgoingsUseCase,
    private val saveOutgoing: SaveOutgoingUseCase,
    private val deleteOutgoing: DeleteOutgoingUseCase,
    private val calculateMonthlyTotal: CalculateMonthlyTotalUseCase,
    private val calculateRemainingToPay: CalculateRemainingToPayThisMonthUseCase,
) : OutgoingPresenter() {

    private val _state = MutableStateFlow(OutgoingState(isLoading = true))
    override val state: StateFlow<OutgoingState> = _state.asStateFlow()

    private val onCoroutineError: (AppException) -> Unit = { error ->
        _state.update { it.copy(isLoading = false, error = error) }
    }

    init {
        startObservingData()
    }

    private fun startObservingData() {
        viewModelScope.safeLaunch(onError = onCoroutineError) {

            combine(
                observeActiveOutgoings(),
                calculateMonthlyTotal(),
                calculateRemainingToPay()
            ) { outgoings, monthlyTotal, remainingToPay ->
                Triple(outgoings, monthlyTotal, remainingToPay)
            }.collect { (outgoings, monthlyTotal, remainingToPay) ->
                _state.update { currentState ->
                    currentState.copy(
                        outgoings = outgoings,
                        monthlyTotalInCents = monthlyTotal,
                        remainingToPayThisMonthInCents = remainingToPay,
                        isLoading = false
                    )
                }
            }
        }
    }

    override fun onIntent(intent: OutgoingIntent) {
        when (intent) {
            is OutgoingIntent.Add -> handleAdd(intent)
            is OutgoingIntent.Delete -> handleDelete(intent)
            is OutgoingIntent.DismissError -> {
                _state.update { it.copy(error = null) }
            }
        }
    }

    private fun handleAdd(intent: OutgoingIntent.Add) {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isLoading = true) }

            val result = saveOutgoing(
                name = intent.name,
                amountInCents = intent.amountInCents,
                cycle = intent.cycle,
                billingDay = intent.billingDay,
                billingMonth = intent.billingMonth
            )

            when (result) {
                is Result.Success -> {
                    _state.update { it.copy(isLoading = false, error = null) }
                }
                is Result.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.error) }
                }
            }
        }
    }

    private fun handleDelete(intent: OutgoingIntent.Delete) {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            val result = deleteOutgoing(intent.id)
            if (result is Result.Error) {
                _state.update { it.copy(error = result.error) }
            }
        }
    }
}