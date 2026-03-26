package fr.abknative.outgo.outgoing.impl.presenter

import androidx.lifecycle.viewModelScope
import fr.abknative.outgo.auth.api.usecase.ObserveUserSessionUseCase
import fr.abknative.outgo.core.api.KeyValueStorage
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.core.api.extensions.safeLaunch
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.outgoing.api.presenter.OutgoingIntent
import fr.abknative.outgo.outgoing.api.presenter.OutgoingPresenter
import fr.abknative.outgo.outgoing.api.presenter.OutgoingState
import fr.abknative.outgo.outgoing.api.presenter.SyncUiState
import fr.abknative.outgo.outgoing.api.repository.BudgetRepository
import fr.abknative.outgo.outgoing.api.usecase.*
import fr.abknative.outgo.sync.api.SyncManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

internal class OutgoingPresenterImpl(
    private val observeActiveOutgoings: ObserveActiveOutgoingsUseCase,
    private val saveOutgoing: SaveOutgoingUseCase,
    private val deleteOutgoing: DeleteOutgoingUseCase,
    private val calculateTotalOutgoings: CalculateTotalOutgoingsUseCase,
    private val calculateRemainingToPay: CalculateRemainingToPayUseCase,
    private val calculateDisposableIncome: CalculateDisposableIncomeUseCase,
    private val observeUserSession: ObserveUserSessionUseCase,
    private val updateIncome: UpdateIncomeUseCase,
    private val budgetRepository: BudgetRepository,
    private val timeProvider: TimeProvider,
    private val syncManager: SyncManager,
    private val storage: KeyValueStorage
) : OutgoingPresenter() {

    private val heroExpandedKey = "hero_section_expanded"
    private val _state = MutableStateFlow(
        OutgoingState(
            isLoading = true,
            isHeroExpanded = storage.getBoolean(heroExpandedKey, true),
            currentDay = timeProvider.dayOfMonth(),
            currentMonth = timeProvider.monthValue(),
            selectedMonth = timeProvider.monthValue()
        )
    )
    override val state: StateFlow<OutgoingState> = _state.asStateFlow()

    private val selectedMonthFlow = MutableStateFlow(timeProvider.monthValue())

    private val onCoroutineError: (AppException) -> Unit = { error ->
        _state.update {
            it.copy(
                isLoading = false,
                error = error,
                syncState = if (it.syncState.isUnauthenticated) SyncUiState.UNAUTHENTICATED else SyncUiState.ERROR
            )
        }
    }

    init {
        startObservingSession()
        startObservingData()
    }

    private fun startObservingSession() {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            observeUserSession().collect { session ->
                _state.update { currentState ->
                    if (session == null) {
                        currentState.copy(syncState = SyncUiState.UNAUTHENTICATED)
                    } else {
                        val nextState = if (currentState.syncState.isUnauthenticated) SyncUiState.UP_TO_DATE else currentState.syncState
                        currentState.copy(syncState = nextState)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun startObservingData() {
        viewModelScope.safeLaunch(onError = onCoroutineError) {

            combine(
                selectedMonthFlow.flatMapLatest { month ->
                    observeActiveOutgoings(month).map { list -> Pair(month, list) }
                },
                budgetRepository.observeBudget()
            ) { (selectedMonth, outgoings), budget ->
                val income = budget?.monthlyIncomeInCents ?: 0L
                val total = calculateTotalOutgoings(outgoings)
                val remaining = calculateRemainingToPay(outgoings, selectedMonth)
                val disposable = calculateDisposableIncome(income, total)

                _state.update {
                    it.copy(
                        outgoings = outgoings,
                        totalOutgoingsInCents = total,
                        remainingToPayInCents = remaining,
                        disposableIncomeInCents = disposable,
                        monthlyIncomeInCents = income,
                        selectedMonth = selectedMonth,
                        isLoading = false
                    )
                }
            }.collect()
        }
    }

    override fun onIntent(intent: OutgoingIntent) {
        when (intent) {
            is OutgoingIntent.Save -> handleAdd(intent)
            is OutgoingIntent.SelectMonth -> selectedMonthFlow.value = intent.month
            is OutgoingIntent.Delete -> handleDelete(intent)
            is OutgoingIntent.UpdateIncome -> handleUpdateIncome(intent)
            is OutgoingIntent.ToggleHeroSection -> {
                storage.putBoolean(heroExpandedKey, intent.isExpanded)
                _state.update { it.copy(isHeroExpanded = intent.isExpanded) }
            }
            is OutgoingIntent.Refresh -> handleRefresh()
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

    private fun handleRefresh() {
        if (_state.value.syncState.isUnauthenticated || _state.value.syncState.isInProgress) return
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(syncState = SyncUiState.IN_PROGRESS) }

            val result = syncManager.syncAll()

            _state.update {
                it.copy(
                    syncState = if (result is Result.Error) SyncUiState.ERROR else SyncUiState.UP_TO_DATE,
                    error = (result as? Result.Error)?.error
                )
            }
        }
    }
}