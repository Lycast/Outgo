package fr.abknative.outgo.shell.impl

import androidx.lifecycle.viewModelScope
import fr.abknative.outgo.core.api.KeyValueStorage
import fr.abknative.outgo.core.api.extensions.safeLaunch
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.core.api.nav.AppStep
import fr.abknative.outgo.core.api.nav.NavCoordinator
import fr.abknative.outgo.shell.api.ShellIntent
import fr.abknative.outgo.shell.api.ShellPresenter
import fr.abknative.outgo.shell.api.ShellState
import fr.abknative.outgo.subscription.api.FeatureManager
import fr.abknative.outgo.sync.api.SyncEvent
import fr.abknative.outgo.sync.api.SyncManager
import fr.abknative.outgo.sync.api.SyncOrchestrator
import fr.abknative.outgo.sync.api.usecase.ObserveSyncStateUseCase
import fr.abknative.outgo.wallet.api.usecase.ObserveWalletsUseCase
import kotlinx.coroutines.flow.*

internal class ShellPresenterImpl(
    private val observeSyncState: ObserveSyncStateUseCase,
    private val syncManager: SyncManager,
    private val syncOrchestrator: SyncOrchestrator,
    private val featureManager: FeatureManager,
    private val observeWallets: ObserveWalletsUseCase,
    private val coordinator: NavCoordinator,
    private val storage: KeyValueStorage
) : ShellPresenter() {

    private val themeKey = "app_is_dark_mode"

    private val _state = MutableStateFlow(ShellState())
    override val state: StateFlow<ShellState> = _state.asStateFlow()

    private val onCoroutineError: (AppException) -> Unit = { error ->
        _state.update { it.copy(error = error) }
    }

    init {
        startObservingSyncState()
        startObservingPremiumStatus()
        startGlobalNavigationLogic()
        startObservingCriticalSyncErrors()
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

    private fun startGlobalNavigationLogic() {
        viewModelScope.safeLaunch(onError = onCoroutineError) {

            combine(
                observeWallets(),
                coordinator.state.map { it.currentStep }.distinctUntilChanged()
            ) { wallets, currentStep ->
                Pair(wallets, currentStep)
            }.collect { (wallets, currentStep) ->

                _state.update { it.copy(activeWalletId = wallets.firstOrNull()?.id) }

                if (wallets.isEmpty()) {
                    if (currentStep == AppStep.Month || currentStep == AppStep.Splash) {
                        coordinator.replaceRoot(AppStep.Onboarding)
                    }
                } else {
                    if (currentStep == AppStep.Onboarding || currentStep == AppStep.Splash) {
                        coordinator.replaceRoot(AppStep.Month)
                    }
                }
            }
        }
    }

    private fun startObservingCriticalSyncErrors() {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            syncOrchestrator.syncEvents.collect { event ->
                when (event) {
                    is SyncEvent.Error -> {
                        _state.update { it.copy(error = event.exception) }
                    }
                }
            }
        }
    }

    override fun onIntent(intent: ShellIntent) {
        when (intent) {
            is ShellIntent.OpenOperationForm -> {
                _state.update { it.copy(operationPayload = intent.payload) }
            }
            is ShellIntent.CloseOperationForm -> {
                _state.update { it.copy(operationPayload = null) }
            }
            is ShellIntent.RefreshSync -> handleRefreshSync()
            is ShellIntent.DismissError -> _state.update { it.copy(error = null) }
            is ShellIntent.InitTheme -> handleInitTheme(intent.systemDefaultIsDark)
            is ShellIntent.UpdateDarkMode -> handleUpdateTheme(intent.isDarkMode)
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

    private fun handleInitTheme(systemDefaultIsDark: Boolean) {
        if (!_state.value.isThemeInitialized) {
            val savedTheme = storage.getBoolean(themeKey, systemDefaultIsDark)
            _state.update {
                it.copy(
                    isDarkMode = savedTheme,
                    isThemeInitialized = true
                )
            }
        }
    }

    private fun handleUpdateTheme(isDarkMode: Boolean) {
        storage.putBoolean(themeKey, isDarkMode)
        _state.update { it.copy(isDarkMode = isDarkMode) }
    }
}