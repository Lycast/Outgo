package fr.abknative.outgo.list.impl

import androidx.lifecycle.viewModelScope
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.core.api.extensions.safeLaunch
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.list.api.ListIntent
import fr.abknative.outgo.list.api.ListPresenter
import fr.abknative.outgo.list.api.ListState
import fr.abknative.outgo.list.api.OperationFilter
import fr.abknative.outgo.subscription.api.FeatureManager
import fr.abknative.outgo.wallet.api.usecase.DeleteOperationUseCase
import fr.abknative.outgo.wallet.api.usecase.ObserveActiveOperationsUseCase
import fr.abknative.outgo.wallet.api.usecase.ObserveWalletsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

internal class ListPresenterImpl(
    private val observeActiveOperations: ObserveActiveOperationsUseCase,
    private val observeWallets: ObserveWalletsUseCase,
    private val deleteOperation: DeleteOperationUseCase,
    private val mapper: ListStateMapper,
    private val timeProvider: TimeProvider,
    private val featureManager: FeatureManager
) : ListPresenter() {

    // Flows de navigation
    private val selectedMonthFlow = MutableStateFlow(timeProvider.monthValue())
    private val selectedYearFlow = MutableStateFlow(timeProvider.yearValue())
    private val currentFilterFlow = MutableStateFlow(OperationFilter.ALL)
    private val isPremiumFlow = featureManager.isPremiumFlow

    private val _state = MutableStateFlow(
        ListState(
            isLoading = true,
            currentDay = timeProvider.dayOfMonth(),
            currentMonth = timeProvider.monthValue(),
            selectedMonth = timeProvider.monthValue(),
            selectedYear = timeProvider.yearValue(),
            isPremium = false
        )
    )
    override val state: StateFlow<ListState> = _state.asStateFlow()

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
                            mapper.mapToState(
                                currentOperations = ops,
                                input = input,
                            )
                        }
                }
                .collect { newState ->
                    _state.value = newState
                }
        }
    }

    override fun onIntent(intent: ListIntent) {
        when (intent) {
            is ListIntent.UpdateFilter -> currentFilterFlow.value = intent.filter
            is ListIntent.NavigateMonth -> handleNavigateMonth(intent.isNext)
            is ListIntent.Delete -> handleDelete(intent)
            is ListIntent.DismissError -> _state.update { it.copy(error = null) }
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

    private fun handleDelete(intent: ListIntent.Delete) {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            val result = deleteOperation(intent.id)
            if (result is Result.Error) _state.update { it.copy(error = result.error) }
        }
    }
}