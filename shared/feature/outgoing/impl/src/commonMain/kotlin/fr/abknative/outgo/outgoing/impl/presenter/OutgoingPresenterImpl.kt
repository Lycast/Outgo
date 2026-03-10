package fr.abknative.outgo.outgoing.impl.presenter

import androidx.lifecycle.viewModelScope
import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.core.api.Result
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.core.api.extensions.safeLaunch
import fr.abknative.outgo.outgoing.api.presenter.OutgoingIntent
import fr.abknative.outgo.outgoing.api.presenter.OutgoingPresenter
import fr.abknative.outgo.outgoing.api.presenter.OutgoingState
import fr.abknative.outgo.outgoing.api.repository.BudgetRepository
import fr.abknative.outgo.outgoing.api.usecase.*
import kotlinx.coroutines.flow.*

internal class OutgoingPresenterImpl(
    private val observeActiveOutgoings: ObserveActiveOutgoingsUseCase,
    private val saveOutgoing: SaveOutgoingUseCase,
    private val deleteOutgoing: DeleteOutgoingUseCase,
    private val calculateTotalOutgoings: CalculateTotalOutgoingsUseCase,
    private val calculateRemainingToPay: CalculateRemainingToPayUseCase,
    private val calculateDisposableIncome: CalculateDisposableIncomeUseCase,
    private val updateIncome: UpdateIncomeUseCase,
    private val budgetRepository: BudgetRepository,
    private val timeProvider: TimeProvider
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

            val currentDay = timeProvider.dayOfMonth()
            val currentMonth = timeProvider.monthValue()

            combine(
                observeActiveOutgoings(),
                calculateTotalOutgoings(),
                calculateRemainingToPay(),
                calculateDisposableIncome(),
                budgetRepository.observeBudget().map { it.monthlyIncomeInCents }
            ) { outgoings, total, remaining, disposable, income ->
                _state.update { it.copy(
                    outgoings = outgoings,
                    totalOutgoingsInCents = total,
                    remainingToPayInCents = remaining,
                    disposableIncomeInCents = disposable,
                    monthlyIncomeInCents = income,
                    currentDay = currentDay,
                    currentMonth = currentMonth,
                    isLoading = false
                ) }
            }.collect()
        }
    }

    override fun onIntent(intent: OutgoingIntent) {
        when (intent) {
            is OutgoingIntent.Save -> handleAdd(intent)
            is OutgoingIntent.Delete -> handleDelete(intent)
            is OutgoingIntent.UpdateIncome -> handleUpdateIncome(intent)
            is OutgoingIntent.DismissError -> { _state.update { it.copy(error = null) } }
        }
    }

    private fun handleAdd(intent: OutgoingIntent.Save) {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isLoading = true) }

            val result = saveOutgoing(
                id = intent.id,
                name = intent.name,
                amountInCents = intent.amountInCents,
                recurrence = intent.recurrence,
                dueDay = intent.dueDay,
                dueMonth = intent.dueMonth
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

    private fun handleUpdateIncome(intent: OutgoingIntent.UpdateIncome) {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            updateIncome(intent.amountInCents)
        }
    }
}