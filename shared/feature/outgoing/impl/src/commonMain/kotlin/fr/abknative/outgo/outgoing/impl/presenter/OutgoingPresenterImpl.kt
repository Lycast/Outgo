package fr.abknative.outgo.outgoing.impl.presenter

import fr.abknative.outgo.core.api.AppDispatchers
import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.core.api.Result
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.outgoing.api.presenter.OutgoingIntent
import fr.abknative.outgo.outgoing.api.presenter.OutgoingPresenter
import fr.abknative.outgo.outgoing.api.presenter.OutgoingState
import fr.abknative.outgo.outgoing.api.usecase.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import fr.abknative.outgo.core.api.extensions.safeLaunch

internal class OutgoingPresenterImpl(
    private val observeActiveOutgoings: ObserveActiveOutgoingsUseCase,
    private val saveOutgoing: SaveOutgoingUseCase,
    private val deleteOutgoing: DeleteOutgoingUseCase,
    private val calculateMonthlyTotal: CalculateMonthlyTotalUseCase,
    private val calculateRemainingToPay: CalculateRemainingToPayThisMonthUseCase,
    private val timeProvider: TimeProvider,
    dispatchers: AppDispatchers
) : OutgoingPresenter {

    private val presenterScope = CoroutineScope(SupervisorJob() + dispatchers.main)

    private val _state = MutableStateFlow(OutgoingState(isLoading = true))
    override val state: StateFlow<OutgoingState> = _state.asStateFlow()

    private val onCoroutineError: (AppException) -> Unit = { error ->
        _state.update { it.copy(isLoading = false, error = error) }
    }

    init {
        startObservingData()
    }

    private fun startObservingData() {
        presenterScope.safeLaunch(onCoroutineError) {
            observeActiveOutgoings().collect { list ->
                _state.update { it.copy(outgoings = list, isLoading = false) }
            }
        }

        presenterScope.safeLaunch(onCoroutineError) {
            calculateMonthlyTotal().collect { total ->
                _state.update { it.copy(monthlyTotalInCents = total) }
            }
        }

        presenterScope.safeLaunch(onCoroutineError) {
            val currentDay = timeProvider.dayOfMonth()
            calculateRemainingToPay(currentDay).collect { remaining ->
                _state.update { it.copy(remainingToPayThisMonthInCents = remaining) }
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
        presenterScope.safeLaunch(onCoroutineError) {
            _state.update { it.copy(isLoading = true) }

            val result = saveOutgoing(
                name = intent.name,
                amountInCents = intent.amountInCents,
                cycle = intent.cycle,
                billingDay = intent.billingDay
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
        presenterScope.safeLaunch(onCoroutineError) {
            val result = deleteOutgoing(intent.id)
            if (result is Result.Error) {
                _state.update { it.copy(error = result.error) }
            }
        }
    }
}