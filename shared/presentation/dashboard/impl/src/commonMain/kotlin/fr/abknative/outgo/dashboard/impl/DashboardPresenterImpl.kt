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
import fr.abknative.outgo.subscription.api.FeatureManager
import fr.abknative.outgo.sync.api.SyncManager
import fr.abknative.outgo.sync.api.usecase.ObserveSyncStateUseCase
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
    private val observeSyncState: ObserveSyncStateUseCase,
    private val saveWallet: SaveWalletUseCase,
    private val timeProvider: TimeProvider,
    private val syncManager: SyncManager,
    private val storage: KeyValueStorage,
    private val featureManager: FeatureManager
) : DashboardPresenter() {

    private val heroExpandedKey = "hero_section_expanded"
    private val _state = MutableStateFlow(
        DashboardState(
            isLoading = true,
            isHeroExpanded = storage.getBoolean(heroExpandedKey, true),
            currentDay = timeProvider.dayOfMonth(),
            currentMonth = timeProvider.monthValue(),
            selectedMonth = timeProvider.monthValue(),
            selectedYear = timeProvider.yearValue()
        )
    )
    override val state: StateFlow<DashboardState> = _state.asStateFlow()

    private val selectedMonthFlow = MutableStateFlow(timeProvider.monthValue(timeProvider.now()))
    private val selectedYearFlow = MutableStateFlow(timeProvider.yearValue(timeProvider.now()))

    private val onCoroutineError: (AppException) -> Unit = { error ->
        _state.update { it.copy(isLoading = false, error = error) }
    }

    init {
        startObservingSyncState()
        startObservingPremiumStatus()
        startObservingData()
    }

    private fun startObservingSyncState() {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            observeSyncState().collect { globalSyncState ->
                _state.update { it.copy(syncState = globalSyncState) }
            }
        }
    }

    private fun startObservingPremiumStatus() {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            featureManager.isPremiumFlow.collect { isPremium ->
                _state.update { it.copy(isPremium = isPremium) }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun startObservingData() {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            combine(
                observeWallets(),
                selectedMonthFlow,
                selectedYearFlow
            ) { wallets, month, year ->
                Triple(wallets.firstOrNull(), month, year)
            }.flatMapLatest { (wallet, month, year) ->
                if (wallet == null) {
                    flowOf(emptyList())
                    // todo c'est ici qu'il faudra brancher la navigation vers la première connexion
                } else {
                    _state.update {
                        it.copy(
                            activeWalletId = wallet.id,
                            activeWalletName = wallet.name,
                            walletCreationMonth = timeProvider.monthValue(wallet.createdAt),
                            walletCreationYear = timeProvider.yearValue(wallet.createdAt)
                        )
                    }
                    observeActiveOperations(wallet.id, month, year)
                }
            }.collect { projectedOperations ->
                val month = selectedMonthFlow.value
                val year = selectedYearFlow.value
                val dashboardData = calculateDashboardData(projectedOperations, month, year)

                _state.update {
                    it.copy(
                        operations = projectedOperations,
                        monthlyIncomeInCents = projectedOperations
                            .filter { it.operation.type == OperationType.INCOME }
                            .sumOf { it.operation.amountInCents },
                        totalOutgoingsInCents = dashboardData.totalExpensesInCents,
                        remainingToPayInCents = dashboardData.remainingToPayInCents,
                        disposableIncomeInCents = dashboardData.disposableIncomeInCents,
                        selectedMonth = month,
                        selectedYear = year,
                        isLoading = false
                    )
                }
            }
        }
    }

    override fun onIntent(intent: DashboardIntent) {
        when (intent) {
            is DashboardIntent.SaveOperation -> handleSaveOperation(intent)
            is DashboardIntent.SaveWalletAndIncome -> handleSaveWalletAndIncome(intent)
            is DashboardIntent.SelectMonth -> {
                selectedMonthFlow.value = intent.month
                selectedYearFlow.value = intent.year
            }
            is DashboardIntent.Delete -> handleDelete(intent)
            is DashboardIntent.ToggleHeroSection -> {
                storage.putBoolean(heroExpandedKey, intent.isExpanded)
                _state.update { it.copy(isHeroExpanded = intent.isExpanded) }
            }
            is DashboardIntent.Refresh -> handleRefreshSync()
            is DashboardIntent.DismissError -> { _state.update { it.copy(error = null) } }
        }
    }

    private fun handleSaveOperation(intent: DashboardIntent.SaveOperation) {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isLoading = true) }
            val result = saveOperation(
                id = intent.id,
                walletId = intent.walletId,
                name = intent.name,
                amountInCents = intent.amountInCents,
                type = intent.type,
                recurrence = intent.recurrence,
                startDate = intent.startDate,
                endDate = intent.endDate
            )
            handleOperationResult(result)
        }
    }

    private fun handleSaveWalletAndIncome(intent: DashboardIntent.SaveWalletAndIncome) {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isLoading = true) }

            val walletResult = saveWallet(id = intent.walletId, name = intent.walletName)
            if (walletResult is Result.Error) {
                _state.update { it.copy(isLoading = false, error = walletResult.error) }
                return@safeLaunch
            }

            val existingIncome = _state.value.operations.firstOrNull { it.operation.type == OperationType.INCOME }
            val operationResult = saveOperation(
                id = existingIncome?.operation?.id,
                walletId = intent.walletId,
                name = "Revenu Principal",
                amountInCents = intent.incomeAmountInCents,
                type = OperationType.INCOME,
                recurrence = Recurrence.MONTHLY,
                startDate = existingIncome?.operation?.startDate ?: intent.startDate,
                endDate = null
            )
            handleOperationResult(operationResult)
        }
    }

    private fun handleDelete(intent: DashboardIntent.Delete) {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            val result = deleteOperation(intent.id)
            if (result is Result.Error) {
                _state.update { it.copy(error = result.error) }
            }
        }
    }

    private fun handleRefreshSync() {
        if (_state.value.syncState.isUnauthenticated || _state.value.syncState.isInProgress) return

        viewModelScope.safeLaunch(onError = onCoroutineError) {
            val result = syncManager.syncAll()
            if (result is Result.Error) {
                _state.update { it.copy(error = result.error) }
            }
        }
    }

    private suspend fun handleOperationResult(result: Result<Unit, AppException>) {
        _state.update { it.copy(isLoading = false) }

        if (result is Result.Success) {
            _state.update { it.copy(error = null) }
        } else if (result is Result.Error) {
            _state.update { it.copy(error = result.error) }
        }
    }
}