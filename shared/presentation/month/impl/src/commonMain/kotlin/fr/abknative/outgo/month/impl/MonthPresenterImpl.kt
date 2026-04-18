package fr.abknative.outgo.month.impl

import androidx.lifecycle.viewModelScope
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.core.api.extensions.safeLaunch
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.core.api.validators.AmountValidator
import fr.abknative.outgo.core.api.validators.NameValidator
import fr.abknative.outgo.month.api.MonthIntent
import fr.abknative.outgo.month.api.MonthPresenter
import fr.abknative.outgo.month.api.MonthState
import fr.abknative.outgo.subscription.api.FeatureManager
import fr.abknative.outgo.wallet.api.model.operation.OperationType
import fr.abknative.outgo.wallet.api.model.operation.Recurrence
import fr.abknative.outgo.wallet.api.usecase.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlin.math.roundToLong

internal class MonthPresenterImpl(
    private val observeActiveOperations: ObserveProjectedOperationsUseCase,
    private val observeWallets: ObserveWalletsUseCase,
    private val calculateDashboardData: CalculatePeriodStatsUseCase,
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
                .flatMapLatest { wallets ->
                    if (wallets.isEmpty()) {
                        _state.update {
                            MonthState(
                                isLoading = false,
                                selectedMonth = timeProvider.monthValue(),
                                selectedYear = timeProvider.yearValue()
                            )
                        }
                        emptyFlow()
                    } else {
                        combine(selectedMonthFlow, selectedYearFlow, isPremiumFlow) { m, y, p ->
                            MonthPipelineInput(wallet = wallets.first(), month = m, year = y, isPremium = p)
                        }
                            .flatMapLatest { input ->
                                observeActiveOperations(input.wallet.id, input.month, input.year)
                                    .map { ops ->
                                        val stats = calculateDashboardData(ops, input.month, input.year)
                                        Triple(ops, stats, input)
                                    }
                            }
                    }
                }
                .collect { (ops, stats, input) ->
                    _state.update { currentState ->
                        mapper.mapToState(currentState, ops, stats, input)
                    }
                }
        }
    }

    override fun onIntent(intent: MonthIntent) {
        when (intent) {
            is MonthIntent.OpenEditWalletDialog -> handleOpenEditDialog()
            is MonthIntent.CloseEditWalletDialog -> _state.update { it.copy(isEditWalletDialogVisible = false) }
            is MonthIntent.UpdateEditWalletName -> {
                val validName = NameValidator.validate(intent.name)
                _state.update { it.copy(editWalletNameBuffer = validName) }
            }
            is MonthIntent.UpdateEditWalletAmount -> {
                AmountValidator.validate(intent.amount)?.let { validAmount ->
                    _state.update { it.copy(editWalletAmountBuffer = validAmount) }
                }
            }
            is MonthIntent.SubmitWalletAndIncome -> handleSubmitWallet()
            is MonthIntent.RenameWallet -> handleRenameWallet(intent)
            is MonthIntent.NavigateMonth -> handleNavigateMonth(intent.isNext)
            is MonthIntent.DismissError -> _state.update { it.copy(error = null) }
        }
    }

    private fun handleOpenEditDialog() {
        val currentState = _state.value

        val initialAmountString = if (currentState.monthlyIncomeInCents > 0) {
            val centsStr = currentState.monthlyIncomeInCents.toString()
            if (centsStr.length >= 3) {
                centsStr.dropLast(2) + "." + centsStr.takeLast(2)
            } else {
                "0." + centsStr.padStart(2, '0')
            }
        } else ""

        _state.update {
            it.copy(
                isEditWalletDialogVisible = true,
                editWalletNameBuffer = currentState.activeWalletName,
                editWalletAmountBuffer = initialAmountString
            )
        }
    }

    private fun handleSubmitWallet() {
        val currentState = _state.value
        val walletId = currentState.activeWalletId ?: return

        if (currentState.editWalletNameBuffer.isBlank() || currentState.editWalletAmountBuffer.isBlank()) return

        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isLoading = true, isEditWalletDialogVisible = false) }

            val sanitizedAmount = currentState.editWalletAmountBuffer.replace(",", ".")
            val amountInCents = ((sanitizedAmount.toDoubleOrNull() ?: 0.0) * 100).roundToLong()

            saveWallet(id = walletId, name = currentState.editWalletNameBuffer.trim())

            val result = saveOperation(
                id = currentState.incomeOperationId,
                walletId = walletId,
                name = currentState.incomeOperationName,
                amountInCents = amountInCents,
                type = OperationType.INCOME,
                recurrence = Recurrence.MONTHLY,
                startDate = currentState.incomeOperationStartDate ?: timeProvider.startOfMonth(currentState.selectedMonth, currentState.selectedYear)
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