package fr.abknative.outgo.list.impl

import androidx.lifecycle.viewModelScope
import fr.abknative.outgo.core.api.KeyValueStorage
import fr.abknative.outgo.core.api.extensions.safeLaunch
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.core.api.time.TimeProvider
import fr.abknative.outgo.list.api.*
import fr.abknative.outgo.subscription.api.FeatureManager
import fr.abknative.outgo.wallet.api.model.presenter.ProjectedOperation
import fr.abknative.outgo.wallet.api.usecase.DeleteOperationUseCase
import fr.abknative.outgo.wallet.api.usecase.ObserveProjectedOperationsUseCase
import fr.abknative.outgo.wallet.api.usecase.ObserveStandardOperationsUseCase
import fr.abknative.outgo.wallet.api.usecase.ObserveWalletsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

internal class ListPresenterImpl(
    private val observeActiveOperations: ObserveProjectedOperationsUseCase,
    private val observeStandardOperations: ObserveStandardOperationsUseCase,
    private val observeWallets: ObserveWalletsUseCase,
    private val deleteOperation: DeleteOperationUseCase,
    private val mapper: ListStateMapper,
    private val timeProvider: TimeProvider,
    private val featureManager: FeatureManager,
    private val storage: KeyValueStorage
) : ListPresenter() {

    // --- Storage Keys ---
    private val keyViewMode = "list_pref_view_mode"
    private val keyProjectedFilter = "list_pref_projected_filter"
    private val keyStandardFilter = "list_pref_standard_filter"

    // --- Reactive Flows ---
    private val isPremiumFlow = featureManager.isPremiumFlow

    private val selectionFlow = MutableStateFlow(
        ListFilterSelection(
            month = timeProvider.monthValue(),
            year = timeProvider.yearValue(),
            viewMode = runCatching {
                ListViewMode.valueOf(storage.getString(keyViewMode) ?: ListViewMode.PROJECTED.name)
            }.getOrDefault(ListViewMode.PROJECTED),
            projectedFilter = runCatching {
                ProjectedFilter.valueOf(storage.getString(keyProjectedFilter) ?: ProjectedFilter.REMAINING.name)
            }.getOrDefault(ProjectedFilter.REMAINING),
            standardFilter = runCatching {
                StandardFilter.valueOf(storage.getString(keyStandardFilter) ?: StandardFilter.ALL.name)
            }.getOrDefault(StandardFilter.ALL)
        )
    )

    private val _state = MutableStateFlow(
        ListState(
            isLoading = true,
            currentDay = timeProvider.dayOfMonth(),
            currentMonth = timeProvider.monthValue(),
            selectedMonth = selectionFlow.value.month,
            selectedYear = selectionFlow.value.year,
            viewMode = selectionFlow.value.viewMode,
            projectedFilter = selectionFlow.value.projectedFilter,
            standardFilter = selectionFlow.value.standardFilter
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
                    combine(selectionFlow, isPremiumFlow) { criteria, isPremium ->
                        PipelineInput(
                            wallet = wallets.first(),
                            month = criteria.month,
                            year = criteria.year,
                            viewMode = criteria.viewMode,
                            projectedFilter = criteria.projectedFilter,
                            standardFilter = criteria.standardFilter,
                            isPremium = isPremium
                        )
                    }
                }
                .flatMapLatest { input ->
                    val operationsFlow = when (input.viewMode) {
                        ListViewMode.PROJECTED -> observeActiveOperations(input.wallet.id, input.month, input.year)
                        ListViewMode.STANDARD -> observeStandardOperations(input.wallet.id).map { rawOps ->
                            rawOps.map { op ->
                                ProjectedOperation(
                                    operation = op,
                                    projectedDate = op.startDate,
                                    formattedDate = timeProvider.formatShortDate(op.startDate)
                                )
                            }
                        }
                    }

                    operationsFlow.map { ops -> mapper.mapToState(ops, input) }
                }
                .collect { newState -> _state.value = newState }
        }
    }

    // --- Intent Handling ---
    override fun onIntent(intent: ListIntent) {
        when (intent) {
            is ListIntent.NavigateMonth -> handleNavigateMonth(intent.isNext)
            is ListIntent.Delete -> handleDelete(intent)
            is ListIntent.DismissError -> _state.update { it.copy(error = null) }

            is ListIntent.SwitchViewMode -> {
                selectionFlow.update { it.copy(viewMode = intent.mode) }
                storage.putString(keyViewMode, intent.mode.name)
            }
            is ListIntent.UpdateProjectedFilter -> {
                selectionFlow.update { it.copy(projectedFilter = intent.filter) }
                storage.putString(keyProjectedFilter, intent.filter.name)
            }
            is ListIntent.UpdateStandardFilter -> {
                selectionFlow.update { it.copy(standardFilter = intent.filter) }
                storage.putString(keyStandardFilter, intent.filter.name)
            }
        }
    }

    private fun handleNavigateMonth(isNext: Boolean) {
        selectionFlow.update { current ->
            if (isNext) {
                if (current.month == 12) current.copy(month = 1, year = current.year + 1)
                else current.copy(month = current.month + 1)
            } else {
                if (current.month == 1) current.copy(month = 12, year = current.year - 1)
                else current.copy(month = current.month - 1)
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