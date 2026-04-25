package fr.abknative.outgo.shell.impl

import androidx.lifecycle.viewModelScope
import fr.abknative.outgo.core.api.KeyValueStorage
import fr.abknative.outgo.core.api.extensions.safeLaunch
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.AppLogger
import fr.abknative.outgo.core.api.nav.AppStep
import fr.abknative.outgo.core.api.nav.NavCoordinator
import fr.abknative.outgo.core.api.time.DateTimeFormatter
import fr.abknative.outgo.core.api.time.TimeProvider
import fr.abknative.outgo.shell.api.ShellIntent
import fr.abknative.outgo.shell.api.ShellPresenter
import fr.abknative.outgo.shell.api.ShellState
import fr.abknative.outgo.shell.api.model.ShellOverlayState
import fr.abknative.outgo.subscription.api.FeatureManager
import fr.abknative.outgo.sync.api.SyncEvent
import fr.abknative.outgo.sync.api.SyncOrchestrator
import fr.abknative.outgo.sync.api.usecase.ObserveSyncStateUseCase
import fr.abknative.outgo.wallet.api.usecase.ObserveWalletsUseCase
import kotlinx.coroutines.flow.*

internal class ShellPresenterImpl(
    private val observeSyncState: ObserveSyncStateUseCase,
    private val syncOrchestrator: SyncOrchestrator,
    private val featureManager: FeatureManager,
    private val observeWallets: ObserveWalletsUseCase,
    private val coordinator: NavCoordinator,
    private val dateTimeFormatter: DateTimeFormatter,
    private val timeProvider: TimeProvider,
    private val storage: KeyValueStorage
) : ShellPresenter() {

    private val themeKey = "app_is_dark_mode"

    private val _state = MutableStateFlow(ShellState())
    override val state: StateFlow<ShellState> = _state.asStateFlow()

    private val onCoroutineError: (AppException) -> Unit = { error ->
        _state.update { it.copy(error = error) }
    }

    init {
        initTodayDate()
        startObservingSyncState()
        startObservingPremiumStatus()
        startGlobalNavigationLogic()
        startObservingCriticalSyncErrors()
    }

    private fun initTodayDate() {
        val now = timeProvider.now()
        val formatted = dateTimeFormatter.formatLongDate(now)
        _state.update { it.copy(todayFormatted = formatted) }
    }

    private fun startObservingSyncState() {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            observeSyncState().collect { globalSyncState ->
                val lastSync = storage.getLong("last_sync_timestamp", 0L)

                _state.update { currentState ->
                    val newOverlay = when {
                        globalSyncState.isInProgress && lastSync == 0L -> ShellOverlayState.LOADING
                        globalSyncState.isError && lastSync == 0L -> ShellOverlayState.ERROR
                        !globalSyncState.isInProgress && currentState.overlayState == ShellOverlayState.LOADING && lastSync != 0L -> ShellOverlayState.NONE
                        else -> currentState.overlayState
                    }

                    currentState.copy(
                        syncState = globalSyncState,
                        overlayState = newOverlay
                    )
                }
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
                coordinator.state.map { it.currentStep }.distinctUntilChanged(),
                observeSyncState()
            ) { wallets, currentStep, syncState ->
                Triple(wallets, currentStep, syncState)
            }.collect { (wallets, currentStep, syncState) ->

                _state.update { it.copy(activeWalletId = wallets.firstOrNull()?.id) }

                // Define Public vs Private steps
                val isPublicStep = currentStep == AppStep.Onboarding
                        || currentStep == AppStep.Login
                        || currentStep == AppStep.Splash
                        || currentStep == AppStep.DeleteAccount
                        || currentStep == AppStep.Settings

                if (wallets.isEmpty()) {
                    if (!syncState.isInProgress && (!isPublicStep || currentStep == AppStep.Splash)) {
                        AppLogger.get()?.i("ShellNav", "No wallets found. Redirecting to Onboarding from $currentStep")
                        coordinator.replaceRoot(AppStep.Onboarding)
                    }
                } else {
                    if (currentStep == AppStep.Onboarding || currentStep == AppStep.Splash) {
                        AppLogger.get()?.i("ShellNav", "Wallets found. Proceeding to App from $currentStep")
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
                        val lastSync = storage.getLong("last_sync_timestamp", 0L)
                        _state.update { it.copy(
                            error = event.exception,
                            overlayState = if (lastSync == 0L) ShellOverlayState.ERROR else ShellOverlayState.NONE
                        ) }
                    }
                    is SyncEvent.ConflictRequiresResolution -> {
                        _state.update { it.copy(overlayState = ShellOverlayState.CONFLICT) }
                    }
                }
            }
        }
    }

    override fun onIntent(intent: ShellIntent) {
        when (intent) {
            is ShellIntent.OpenOperationForm -> {
                _state.update { it.copy(
                    operationPayload = intent.payload,
                    isOperationFormVisible = true
                ) }
            }
            is ShellIntent.CloseOperationForm -> {
                _state.update { it.copy(
                    operationPayload = null,
                    isOperationFormVisible = false
                ) }
            }
            is ShellIntent.CancelSyncAndLogout -> handleResolveConflictCancelLogin()
            is ShellIntent.RefreshSync -> handleRefreshSync()
            is ShellIntent.ResolveConflictDownloadCloud -> handleResolveConflictDownloadCloud()
            is ShellIntent.ResolveConflictCancelLogin -> handleResolveConflictCancelLogin()
            is ShellIntent.ShowGlobalError -> _state.update { it.copy(globalErrorMessage = intent.message) }
            is ShellIntent.DismissError -> _state.update { it.copy(error = null, globalErrorMessage = null) }
            is ShellIntent.InitTheme -> handleInitTheme(intent.systemDefaultIsDark)
            is ShellIntent.UpdateDarkMode -> handleUpdateTheme(intent.isDarkMode)
        }
    }

    private fun handleRefreshSync() {
        if (_state.value.syncState.isUnauthenticated || _state.value.syncState.isInProgress) return
        syncOrchestrator.triggerManualSync()
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

    private fun handleResolveConflictDownloadCloud() {
        _state.update { it.copy(overlayState = ShellOverlayState.NONE) }
        syncOrchestrator.resolveConflictDownloadCloud()
    }

    private fun handleResolveConflictCancelLogin() {
        _state.update { it.copy(overlayState = ShellOverlayState.NONE) }
        syncOrchestrator.resolveConflictCancelLogin()
    }
}