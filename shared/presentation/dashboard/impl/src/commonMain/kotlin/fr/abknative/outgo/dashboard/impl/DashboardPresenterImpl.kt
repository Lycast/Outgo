package fr.abknative.outgo.dashboard.impl

import androidx.lifecycle.viewModelScope
import fr.abknative.outgo.auth.api.usecase.ObserveUserSessionUseCase
import fr.abknative.outgo.core.api.KeyValueStorage
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.core.api.extensions.safeLaunch
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.dashboard.api.DashboardIntent
import fr.abknative.outgo.dashboard.api.DashboardPresenter
import fr.abknative.outgo.dashboard.api.DashboardState
import fr.abknative.outgo.dashboard.api.SyncUiState
import fr.abknative.outgo.sync.api.SyncManager
import fr.abknative.outgo.wallet.api.repository.BudgetRepository
import fr.abknative.outgo.wallet.api.usecase.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

internal class DashboardPresenterImpl(
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
) : DashboardPresenter() {

    private val heroExpandedKey = "hero_section_expanded"
    private val _state = MutableStateFlow(
        DashboardState(
            isLoading = true,
            isHeroExpanded = storage.getBoolean(heroExpandedKey, true),
            currentDay = timeProvider.dayOfMonth(),
            currentMonth = timeProvider.monthValue(),
            selectedMonth = timeProvider.monthValue()
        )
    )
    override val state: StateFlow<DashboardState> = _state.asStateFlow()

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

    override fun onIntent(intent: DashboardIntent) {
        when (intent) {
            is DashboardIntent.Save -> handleAdd(intent)
            is DashboardIntent.SelectMonth -> selectedMonthFlow.value = intent.month
            is DashboardIntent.Delete -> handleDelete(intent)
            is DashboardIntent.UpdateIncome -> handleUpdateIncome(intent)
            is DashboardIntent.ToggleHeroSection -> {
                storage.putBoolean(heroExpandedKey, intent.isExpanded)
                _state.update { it.copy(isHeroExpanded = intent.isExpanded) }
            }
            is DashboardIntent.Refresh -> handleRefresh()
            is DashboardIntent.DismissError -> { _state.update { it.copy(error = null) } }

        }
    }

    private fun handleAdd(intent: DashboardIntent.Save) {
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

    private fun handleDelete(intent: DashboardIntent.Delete) {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            val result = deleteOutgoing(intent.id)
            if (result is Result.Error) {
                _state.update { it.copy(error = result.error) }
            }
        }
    }

    private fun handleUpdateIncome(intent: DashboardIntent.UpdateIncome) {
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