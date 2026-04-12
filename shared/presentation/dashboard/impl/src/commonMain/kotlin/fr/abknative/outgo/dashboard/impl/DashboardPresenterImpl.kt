package fr.abknative.outgo.dashboard.impl

import androidx.lifecycle.viewModelScope
import fr.abknative.outgo.core.api.KeyValueStorage
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.core.api.extensions.safeLaunch
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.dashboard.api.DashboardIntent
import fr.abknative.outgo.dashboard.api.DashboardPresenter
import fr.abknative.outgo.dashboard.api.DashboardState
import fr.abknative.outgo.dashboard.api.OperationFilter
import fr.abknative.outgo.wallet.api.model.operation.OperationType
import fr.abknative.outgo.wallet.api.model.operation.Recurrence
import fr.abknative.outgo.wallet.api.usecase.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

internal class DashboardPresenterImpl(
    private val observeActiveOperations: ObserveActiveOperationsUseCase,
    private val observeWallets: ObserveWalletsUseCase,
    private val saveOperation: SaveOperationUseCase,
    private val deleteOperation: DeleteOperationUseCase,
    private val calculateDashboardData: CalculateDashboardDataUseCase,
    private val saveWallet: SaveWalletUseCase,
    private val mapper: DashboardStateMapper,
    private val timeProvider: TimeProvider,
    private val storage: KeyValueStorage
) : DashboardPresenter() {

    private val heroExpandedKey = "hero_section_expanded"

    // Flows de navigation
    private val selectedMonthFlow = MutableStateFlow(timeProvider.monthValue())
    private val selectedYearFlow = MutableStateFlow(timeProvider.yearValue())
    private val currentFilterFlow = MutableStateFlow(OperationFilter.ALL)
    private val isPremiumFlow = MutableStateFlow(true)

    private val _state = MutableStateFlow(
        DashboardState(
            isLoading = true,
            isHeroExpanded = storage.getBoolean(heroExpandedKey, true),
            currentDay = timeProvider.dayOfMonth(),
            currentMonth = timeProvider.monthValue(),
            selectedMonth = timeProvider.monthValue(),
            selectedYear = timeProvider.yearValue(),
            isPremium = isPremiumFlow.value
        )
    )
    override val state: StateFlow<DashboardState> = _state.asStateFlow()

    private val onCoroutineError: (AppException) -> Unit = { error ->
        _state.update { it.copy(isLoading = false, error = error) }
    }

    init {
        startObservingData()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun startObservingData() {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            observeWallets()
                .filter { it.isNotEmpty() }
                .flatMapLatest { wallets ->
                    combine(selectedMonthFlow, selectedYearFlow, currentFilterFlow, isPremiumFlow) { m, y, f, p ->
                        PipelineInput(wallets.first(), m, y, f, p)
                    }
                }
                .flatMapLatest { input ->
                    observeActiveOperations(input.wallet.id, input.month, input.year)
                        .map { ops ->
                            val stats = calculateDashboardData(ops, input.month, input.year)
                            mapper.mapToState(
                                currentOperations = ops,
                                stats = stats,
                                input = input,
                                currentHeroExpanded = storage.getBoolean(heroExpandedKey, true)
                            )
                        }
                }
                .collect { newState ->
                    _state.value = newState
                }
        }
    }

    override fun onIntent(intent: DashboardIntent) {
        when (intent) {
            is DashboardIntent.UpdateFilter -> currentFilterFlow.value = intent.filter
            is DashboardIntent.NavigateMonth -> handleNavigateMonth(intent.isNext)
            is DashboardIntent.SaveOperation -> handleSaveOperation(intent)
            is DashboardIntent.SaveWalletAndIncome -> handleSaveWalletAndIncome(intent)
            is DashboardIntent.SaveWallet -> handleSaveWallet(intent)
            is DashboardIntent.Delete -> handleDelete(intent)
            is DashboardIntent.SelectMonth -> {
                selectedMonthFlow.value = intent.month
                selectedYearFlow.value = intent.year
            }
            is DashboardIntent.ToggleHeroSection -> {
                storage.putBoolean(heroExpandedKey, intent.isExpanded)
                _state.update { it.copy(isHeroExpanded = intent.isExpanded) }
            }
            is DashboardIntent.DismissError -> _state.update { it.copy(error = null) }
        }
    }

    private fun handleNavigateMonth(isNext: Boolean) {
        val currentMonth = selectedMonthFlow.value
        val currentYear = selectedYearFlow.value
        if (isNext) {
            if (currentMonth == 12) {
                selectedMonthFlow.value = 1
                selectedYearFlow.value = currentYear + 1
            } else {
                selectedMonthFlow.value = currentMonth + 1
            }
        } else {
            if (currentMonth == 1) {
                selectedMonthFlow.value = 12
                selectedYearFlow.value = currentYear - 1
            } else {
                selectedMonthFlow.value = currentMonth - 1
            }
        }
    }

    // --- Les actions de base (Sauvegarde/Suppression) ---

    private fun handleSaveOperation(intent: DashboardIntent.SaveOperation) {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            try {
                _state.update { it.copy(isLoading = true) }
            val result = saveOperation(
                id = intent.id, walletId = intent.walletId, name = intent.name,
                amountInCents = intent.amountInCents, type = intent.type,
                recurrence = intent.recurrence, startDate = intent.startDate, endDate = intent.endDate
            )
            handleOperationResult(result)
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun handleSaveWalletAndIncome(intent: DashboardIntent.SaveWalletAndIncome) {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            try {
            _state.update { it.copy(isLoading = true) }
            saveWallet(id = intent.walletId, name = intent.walletName)
            val existingIncome = _state.value.operations.firstOrNull {
                it.operation.type == OperationType.INCOME && it.operation.walletId == intent.walletId
            }
            val result = saveOperation(
                id = existingIncome?.operation?.id, walletId = intent.walletId,
                name = existingIncome?.operation?.name ?: "Revenu",
                amountInCents = intent.incomeAmountInCents, type = OperationType.INCOME,
                recurrence = Recurrence.MONTHLY, startDate = intent.startDate
            )
            handleOperationResult(result)
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun handleSaveWallet(intent: DashboardIntent.SaveWallet) {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isLoading = true) }
            val result = saveWallet(id = intent.id, name = intent.name)
            if (result is Result.Error) _state.update { it.copy(error = result.error) }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun handleDelete(intent: DashboardIntent.Delete) {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            val result = deleteOperation(intent.id)
            if (result is Result.Error) _state.update { it.copy(error = result.error) }
        }
    }

    private fun handleOperationResult(result: Result<Unit, AppException>) {
        _state.update { it.copy(isLoading = false, error = (result as? Result.Error)?.error) }
    }
}