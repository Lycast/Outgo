package fr.abknative.outgo.shell.impl

import androidx.lifecycle.viewModelScope
import fr.abknative.outgo.core.api.extensions.safeLaunch
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.core.api.nav.AppStep
import fr.abknative.outgo.core.api.nav.NavCoordinator
import fr.abknative.outgo.shell.api.ShellIntent
import fr.abknative.outgo.shell.api.ShellPresenter
import fr.abknative.outgo.shell.api.ShellState
import fr.abknative.outgo.subscription.api.FeatureManager
import fr.abknative.outgo.sync.api.SyncManager
import fr.abknative.outgo.sync.api.usecase.ObserveSyncStateUseCase
import fr.abknative.outgo.wallet.api.usecase.ObserveWalletsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class ShellPresenterImpl(
    private val observeSyncState: ObserveSyncStateUseCase,
    private val syncManager: SyncManager,
    private val featureManager: FeatureManager,
    private val observeWallets: ObserveWalletsUseCase,
    private val coordinator: NavCoordinator
) : ShellPresenter() {

    private val _state = MutableStateFlow(ShellState())
    override val state: StateFlow<ShellState> = _state.asStateFlow()

    private val onCoroutineError: (AppException) -> Unit = { error ->
        _state.update { it.copy(error = error) }
    }

    init {
        startObservingSyncState()
        startObservingPremiumStatus()
        startGlobalNavigationLogic()
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
            observeWallets().collect { wallets ->
                val currentStep = coordinator.state.value.currentStep

                if (wallets.isEmpty()) {
                    if (currentStep != AppStep.Onboarding && currentStep != AppStep.Login) {
                        coordinator.replaceRoot(AppStep.Onboarding)
                    }
                } else {
                    if (currentStep == AppStep.Splash || currentStep == AppStep.Onboarding) {
                        coordinator.replaceRoot(AppStep.Dashboard)
                    }
                }
            }
        }
    }

    override fun onIntent(intent: ShellIntent) {
        when (intent) {
            is ShellIntent.RefreshSync -> handleRefreshSync()
            is ShellIntent.DismissError -> _state.update { it.copy(error = null) }
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
}