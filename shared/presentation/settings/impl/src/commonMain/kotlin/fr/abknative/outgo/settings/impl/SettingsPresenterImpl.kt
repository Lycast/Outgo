package fr.abknative.outgo.settings.impl

import androidx.lifecycle.viewModelScope
import fr.abknative.outgo.auth.api.usecase.DeleteAccountUseCase
import fr.abknative.outgo.auth.api.usecase.LogoutUseCase
import fr.abknative.outgo.auth.api.usecase.ObserveUserSessionUseCase
import fr.abknative.outgo.core.api.extensions.safeLaunch
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.core.api.usecase.ClearLocalDataUseCase
import fr.abknative.outgo.settings.api.SettingsIntent
import fr.abknative.outgo.settings.api.SettingsPresenter
import fr.abknative.outgo.settings.api.SettingsState
import fr.abknative.outgo.sync.api.SyncManager
import fr.abknative.outgo.sync.api.usecase.ObserveSyncStateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class SettingsPresenterImpl(
    private val observeUserSession: ObserveUserSessionUseCase,
    private val observeSyncState: ObserveSyncStateUseCase,
    private val logout: LogoutUseCase,
    private val deleteAccount: DeleteAccountUseCase,
    private val clearLocalData: ClearLocalDataUseCase,
    private val syncManager: SyncManager
) : SettingsPresenter() {

    private val _state = MutableStateFlow(SettingsState())
    override val state: StateFlow<SettingsState> = _state.asStateFlow()

    private val onCoroutineError: (AppException) -> Unit = { error ->
        _state.update { it.copy(isProcessing = false, error = error) }
    }

    init {
        startObservingSession()
        startObservingSyncState()
    }

    private fun startObservingSession() {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            observeUserSession().collect { session ->
                _state.update { it.copy(session = session) }
            }
        }
    }

    private fun startObservingSyncState() {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            observeSyncState().collect { globalSyncState ->
                _state.update { it.copy(syncState = globalSyncState) }
            }
        }
    }

    override fun onIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.Logout -> handleLogout()
            is SettingsIntent.DeleteAccount -> handleDeleteAccount(intent)
            is SettingsIntent.PurgeLocalData -> handlePurgeLocalData()
            is SettingsIntent.RefreshSync -> handleRefreshSync()
            is SettingsIntent.DismissError -> _state.update { it.copy(error = null) }
            is SettingsIntent.ResetSuccessFlag -> _state.update { it.copy(actionSuccess = false) }
        }
    }

    private fun handleLogout() {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isProcessing = true) }
            val result = logout()
            if (result is Result.Success) {
                _state.update { it.copy(isProcessing = false, actionSuccess = true) }
            } else if (result is Result.Error) {
                _state.update { it.copy(isProcessing = false, error = result.error) }
            }
        }
    }

    private fun handleDeleteAccount(intent: SettingsIntent.DeleteAccount) {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isProcessing = true) }

            val result = deleteAccount(
                wipeLocal = intent.wipeLocal,
                wipeServer = intent.wipeServer,
                revokeAuth = intent.revokeAuth
            )

            if (result is Result.Success) {
                _state.update { it.copy(isProcessing = false, actionSuccess = true) }
            } else if (result is Result.Error) {
                _state.update { it.copy(isProcessing = false, error = result.error) }
            }
        }
    }

    private fun handlePurgeLocalData() {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isProcessing = true) }
            clearLocalData()

            val logoutResult = logout()

            if (logoutResult is Result.Success) {
                _state.update { it.copy(isProcessing = false, actionSuccess = true) }
            } else if (logoutResult is Result.Error) {
                _state.update { it.copy(isProcessing = false, error = logoutResult.error) }
            }
        }
    }

    private fun handleRefreshSync() {
        // La protection est toujours bonne : pas besoin de lancer une synchro si on n'est pas logué ou déjà en cours
        val currentState = _state.value.syncState
        if (currentState.isUnauthenticated || currentState.isInProgress) return

        viewModelScope.safeLaunch(onError = onCoroutineError) {
            // Pas besoin de modifier l'état ici, syncManager.syncAll() va passer isSyncing à true tout seul
            val result = syncManager.syncAll()

            // On gère uniquement l'affichage des erreurs réseau/serveur ponctuelles via Snackbar (error = ...)
            if (result is Result.Error) {
                _state.update { it.copy(error = result.error) }
            }
        }
    }
}