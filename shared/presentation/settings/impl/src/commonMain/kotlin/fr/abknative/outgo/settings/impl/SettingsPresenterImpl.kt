package fr.abknative.outgo.settings.impl

import androidx.lifecycle.viewModelScope
import fr.abknative.outgo.auth.api.usecase.DeleteAccountUseCase
import fr.abknative.outgo.auth.api.usecase.LogoutUseCase
import fr.abknative.outgo.auth.api.usecase.ObserveUserSessionUseCase
import fr.abknative.outgo.core.api.extensions.safeLaunch
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.CommonError
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.core.api.model.SyncUiState
import fr.abknative.outgo.core.api.usecase.ClearLocalDataUseCase
import fr.abknative.outgo.settings.api.SettingsIntent
import fr.abknative.outgo.settings.api.SettingsPresenter
import fr.abknative.outgo.settings.api.SettingsState
import fr.abknative.outgo.sync.api.SyncManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class SettingsPresenterImpl(
    private val observeUserSession: ObserveUserSessionUseCase,
    private val logout: LogoutUseCase,
    private val deleteAccount: DeleteAccountUseCase,
    private val clearLocalData: ClearLocalDataUseCase,
    private val syncManager: SyncManager
) : SettingsPresenter() {

    private val _state = MutableStateFlow(SettingsState())
    override val state: StateFlow<SettingsState> = _state.asStateFlow()

    private val onCoroutineError: (AppException) -> Unit = { error ->
        _state.update { it.copy(isProcessing = false, error = error) }

        val targetState = if (error is CommonError.NetworkError) SyncUiState.OFFLINE else SyncUiState.ERROR
        updateSyncState(targetState)
    }

    init {
        startObservingSession()
    }

    private fun startObservingSession() {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            observeUserSession().collect { session ->
                if (session == null) {
                    _state.update { it.copy(session = null) }
                    updateSyncState(SyncUiState.UNAUTHENTICATED)
                } else {
                    val wasUnauthenticated = _state.value.syncState.isUnauthenticated
                    _state.update { it.copy(session = session) }

                    if (wasUnauthenticated) {
                        updateSyncState(SyncUiState.PENDING)
                        handleRefreshSync()
                    }
                }
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
            logout()
            _state.update { it.copy(isProcessing = false, actionSuccess = true) }
            updateSyncState(SyncUiState.UNAUTHENTICATED)
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
                logout()
                _state.update { it.copy(isProcessing = false, actionSuccess = true) }
                updateSyncState(SyncUiState.UNAUTHENTICATED)
            } else if (result is Result.Error) {
                _state.update { it.copy(isProcessing = false, error = result.error) }
            }
        }
    }

    private fun handlePurgeLocalData() {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isProcessing = true) }
            clearLocalData()
            logout()
            _state.update { it.copy(isProcessing = false, actionSuccess = true) }
            updateSyncState(SyncUiState.UNAUTHENTICATED)
        }
    }

    private fun handleRefreshSync() {
        val currentState = _state.value.syncState
        if (currentState.isUnauthenticated || currentState.isInProgress) return

        viewModelScope.safeLaunch(onError = onCoroutineError) {
            updateSyncState(SyncUiState.IN_PROGRESS)
            val result = syncManager.syncAll()
            updateSyncStateFromResult(result)
        }
    }

    private fun updateSyncStateFromResult(result: Result<Unit, AppException>) {
        val error = (result as? Result.Error)?.error

        val targetState = when (result) {
            is Result.Success -> SyncUiState.UP_TO_DATE
            is Result.Error -> {
                when (error) {
                    is CommonError.NetworkError -> SyncUiState.OFFLINE
                    is CommonError.Unauthorized -> SyncUiState.UNAUTHENTICATED
                    else -> SyncUiState.ERROR
                }
            }
        }

        _state.update { currentState ->
            val resolved = resolveNewSyncState(currentState.syncState, targetState)
            currentState.copy(
                syncState = resolved,
                error = if (result is Result.Error && error !is CommonError.NetworkError) error else currentState.error
            )
        }
    }

    /**
     * Centralized sync state management with priority rules.
     */
    private fun updateSyncState(newState: SyncUiState) {
        _state.update { currentState ->
            val resolved = resolveNewSyncState(currentState.syncState, newState)
            currentState.copy(syncState = resolved)
        }
    }

    private fun resolveNewSyncState(current: SyncUiState, new: SyncUiState): SyncUiState {
        if (current == SyncUiState.UNAUTHENTICATED && !new.isPending && !new.isInProgress) {
            return SyncUiState.UNAUTHENTICATED
        }
        if (new == SyncUiState.UNAUTHENTICATED) return SyncUiState.UNAUTHENTICATED

        if (new == SyncUiState.OFFLINE || new == SyncUiState.ERROR) return new
        if (current == SyncUiState.OFFLINE && (new == SyncUiState.PENDING || new == SyncUiState.UP_TO_DATE)) {
            return SyncUiState.OFFLINE
        }

        if (new == SyncUiState.IN_PROGRESS) return SyncUiState.IN_PROGRESS

        return new
    }
}