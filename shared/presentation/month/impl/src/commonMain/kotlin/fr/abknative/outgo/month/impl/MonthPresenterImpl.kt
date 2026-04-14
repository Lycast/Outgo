package fr.abknative.outgo.month.impl

import androidx.lifecycle.viewModelScope
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.core.api.extensions.safeLaunch
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.month.api.MonthIntent
import fr.abknative.outgo.month.api.MonthPresenter
import fr.abknative.outgo.month.api.MonthState
import fr.abknative.outgo.subscription.api.FeatureManager
import fr.abknative.outgo.wallet.api.model.operation.OperationType
import fr.abknative.outgo.wallet.api.model.operation.Recurrence
import fr.abknative.outgo.wallet.api.usecase.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

internal class MonthPresenterImpl(
    private val observeActiveOperations: ObserveActiveOperationsUseCase,
    private val observeWallets: ObserveWalletsUseCase,
    private val calculateDashboardData: CalculateDashboardDataUseCase,
    private val saveWallet: SaveWalletUseCase,
    private val saveOperation: SaveOperationUseCase,
    private val featureManager: FeatureManager,
    private val mapper: MonthStateMapper,
    private val timeProvider: TimeProvider
) : MonthPresenter() {

    private val selectedMonthFlow = MutableStateFlow(timeProvider.monthValue())
    private val selectedYearFlow = MutableStateFlow(timeProvider.yearValue())
    private val isPremiumFlow = featureManager.isPremiumFlow

    private val _state = MutableStateFlow(
        MonthState(
            isLoading = true,
            selectedMonth = timeProvider.monthValue(),
            selectedYear = timeProvider.yearValue()
        )
    )
    override val state: StateFlow<MonthState> = _state.asStateFlow()

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
                    combine(selectedMonthFlow, selectedYearFlow, isPremiumFlow) { m, y, p ->
                        MonthPipelineInput(
                            wallet = wallets.first(),
                            month = m,
                            year = y,
                            isPremium = p
                        )
                    }
                }
                .flatMapLatest { input ->
                    observeActiveOperations(input.wallet.id, input.month, input.year)
                        .map { ops ->
                            val stats = calculateDashboardData(ops, input.month, input.year)
                            mapper.mapToState(
                                currentOperations = ops,
                                stats = stats,
                                input = input
                            )
                        }
                }
                .collect { newState ->
                    _state.value = newState
                }
        }
    }

    override fun onIntent(intent: MonthIntent) {
        when (intent) {
            is MonthIntent.SaveWalletAndIncome -> handleSaveWalletAndIncome(intent)
            is MonthIntent.RenameWallet -> handleRenameWallet(intent)
            is MonthIntent.NavigateMonth -> handleNavigateMonth(intent.isNext)
            is MonthIntent.DismissError -> _state.update { it.copy(error = null) }
        }
    }

    private fun handleSaveWalletAndIncome(intent: MonthIntent.SaveWalletAndIncome) {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isLoading = true) }

            saveWallet(id = intent.walletId, name = intent.walletName)

            val result = saveOperation(
                id = intent.incomeOperationId,
                walletId = intent.walletId,
                name = intent.incomeOperationName,
                amountInCents = intent.incomeAmountInCents,
                type = OperationType.INCOME,
                recurrence = Recurrence.MONTHLY,
                startDate = intent.startDate
            )

            _state.update { it.copy(isLoading = false, error = (result as? Result.Error)?.error) }
        }
    }

    private fun handleRenameWallet(intent: MonthIntent.RenameWallet) {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isLoading = true) }
            val result = saveWallet(id = intent.id, name = intent.newName)

            if (result is Result.Error) {
                _state.update { it.copy(isLoading = false, error = result.error) }
            } else {
                _state.update { it.copy(isLoading = false) }
            }
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
}