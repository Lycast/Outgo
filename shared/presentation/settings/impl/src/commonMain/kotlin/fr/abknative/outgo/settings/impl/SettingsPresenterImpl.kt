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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class SettingsPresenterImpl(
    private val observeUserSession: ObserveUserSessionUseCase,
    private val logout: LogoutUseCase,
    private val deleteAccount: DeleteAccountUseCase,
    private val clearLocalData: ClearLocalDataUseCase
) : SettingsPresenter() {

    private val _state = MutableStateFlow(SettingsState())
    override val state: StateFlow<SettingsState> = _state.asStateFlow()

    private val onCoroutineError: (AppException) -> Unit = { error ->
        _state.update { it.copy(isProcessing = false, error = error) }
    }

    init {
        startObservingSession()
    }

    private fun startObservingSession() {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            observeUserSession().collect { session ->
                _state.update { it.copy(session = session) }
            }
        }
    }

    override fun onIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.Logout -> handleLogout()
            is SettingsIntent.DeleteAccount -> handleDeleteAccount(intent)
            is SettingsIntent.PurgeLocalData -> handlePurgeLocalData()
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
}