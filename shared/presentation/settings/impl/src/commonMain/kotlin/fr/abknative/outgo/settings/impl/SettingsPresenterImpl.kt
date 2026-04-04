package fr.abknative.outgo.settings.impl

import androidx.lifecycle.viewModelScope
import fr.abknative.outgo.auth.api.usecase.DeleteAccountUseCase
import fr.abknative.outgo.auth.api.usecase.LogoutUseCase
import fr.abknative.outgo.auth.api.usecase.ObserveUserSessionUseCase
import fr.abknative.outgo.core.api.extensions.safeLaunch
import fr.abknative.outgo.core.api.logs.AppException
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
        _state.update {
            val isNetworkIssue = error is fr.abknative.outgo.core.api.logs.CommonError.NetworkError
            val fallbackState = if (isNetworkIssue) SyncUiState.OFFLINE else SyncUiState.ERROR

            it.copy(
                isProcessing = false,
                error = error,
                syncState = if (it.syncState == SyncUiState.UNAUTHENTICATED) SyncUiState.UNAUTHENTICATED else fallbackState
            )
        }
    }

    init {
        startObservingSession()
    }

    private fun startObservingSession() {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            observeUserSession().collect { session ->
                _state.update { currentState ->
                    if (session == null) {
                        currentState.copy(session = null, syncState = SyncUiState.UNAUTHENTICATED)
                    } else {
                        val nextState = if (currentState.syncState.isUnauthenticated) SyncUiState.UP_TO_DATE else currentState.syncState
                        currentState.copy(session = session, syncState = nextState)
                    }
                }
            }
        }
    }

    override fun onIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.Logout -> handleLogout()
            is SettingsIntent.DeleteAccount -> handleDeleteAccount()
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

            _state.update {
                it.copy(
                    isProcessing = false,
                    actionSuccess = true,
                    syncState = SyncUiState.UNAUTHENTICATED
                )
            }
        }
    }

    private fun handleDeleteAccount() {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isProcessing = true) }

            when (val result = deleteAccount()) {
                is Result.Success -> {
                    logout()
                    _state.update {
                        it.copy(
                            isProcessing = false,
                            actionSuccess = true,
                            syncState = SyncUiState.UNAUTHENTICATED
                        )
                    }
                }
                is Result.Error -> {
                    _state.update { it.copy(isProcessing = false, error = result.error) }
                }
            }
        }
    }

    private fun handlePurgeLocalData() {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isProcessing = true) }

            clearLocalData()

            logout()

            _state.update {
                it.copy(
                    isProcessing = false,
                    actionSuccess = true,
                    syncState = SyncUiState.UNAUTHENTICATED
                )
            }
        }
    }

    private fun handleRefreshSync() {
        val currentState = _state.value.syncState
        if (currentState.isUnauthenticated || currentState.isInProgress) return

        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(syncState = SyncUiState.IN_PROGRESS) }
            val result = syncManager.syncAll()

            _state.update {
                when (result) {
                    is Result.Success -> it.copy(syncState = SyncUiState.UP_TO_DATE, error = null)
                    is Result.Error -> {
                        val isNetworkIssue = result.error is fr.abknative.outgo.core.api.logs.CommonError.NetworkError
                        it.copy(
                            syncState = if (isNetworkIssue) SyncUiState.OFFLINE else SyncUiState.ERROR,
                            error = if (isNetworkIssue) null else result.error
                        )
                    }
                }
            }
        }
    }
}