package fr.abknative.outgo.login.impl

import androidx.lifecycle.viewModelScope
import fr.abknative.outgo.auth.api.AuthError
import fr.abknative.outgo.auth.api.usecase.LoginUseCase
import fr.abknative.outgo.auth.api.usecase.LogoutUseCase
import fr.abknative.outgo.auth.api.usecase.ObserveUserSessionUseCase
import fr.abknative.outgo.auth.api.usecase.RegisterUseCase
import fr.abknative.outgo.core.api.extensions.safeLaunch
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.login.api.LoginEvent
import fr.abknative.outgo.login.api.LoginIntent
import fr.abknative.outgo.login.api.LoginPresenter
import fr.abknative.outgo.login.api.LoginState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

internal class LoginPresenterImpl(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val observeUserSession: ObserveUserSessionUseCase
) : LoginPresenter() {

    private val _state = MutableStateFlow(LoginState(isLoading = false))
    override val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _events = Channel<LoginEvent>(Channel.BUFFERED)
    override val events = _events.receiveAsFlow()

    // PRIVATES VARIABLES FOR RETRY LOGIN AFTER CONFLICT
    private var pendingEmail = ""
    private var pendingPassword = ""
    private var pendingIsRegister = false

    private val onCoroutineError: (AppException) -> Unit = { error ->
        _state.update { it.copy(isLoading = false, error = error) }
    }

    init {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            observeUserSession().collect { session ->
                _state.update { it.copy(session = session) }
            }
        }
    }

    override fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.SubmitRegister -> handleRegister(intent.email, intent.password)
            is LoginIntent.SubmitLogin -> handleLogin(intent.email, intent.password)
            is LoginIntent.LoginWithGoogle, LoginIntent.LoginWithApple -> { /* Rien pour l'instant */ }
            is LoginIntent.Logout -> handleLogout()
            is LoginIntent.DismissError -> _state.update { it.copy(error = null) }
            is LoginIntent.ResolveConflict -> handleResolveConflict()
            is LoginIntent.CancelConflict -> handleCancelConflict()
        }
    }

    private fun handleRegister(email: String, password: String) {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            try {
                _state.update { it.copy(isLoading = true, error = null, showConflictDialog = false) }
                val result = registerUseCase(email, password)
                processAuthResult(result, email, password, isRegister = true)
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun handleLogin(email: String, password: String) {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            try {
                _state.update { it.copy(isLoading = true, error = null, showConflictDialog = false) }
                val result = loginUseCase(email, password)
                processAuthResult(result, email, password, isRegister = false)
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    /**
     * Processes the UseCase return. If a conflict is detected, the action is paused
     * by saving the credentials, and a flag is raised to trigger the popup.
     */
    private fun processAuthResult(
        result: Result<Unit, AppException>,
        email: String,
        password: String,
        isRegister: Boolean
    ) {
        when (result) {
            is Result.Success -> {
                clearPendingAuth()
                _state.update { it.copy(isLoading = false, error = null) }
                _events.trySend(LoginEvent.NavigateBack)
            }
            is Result.Error -> {
                if (result.error is AuthError.DataConflict) {
                    pendingEmail = email
                    pendingPassword = password
                    pendingIsRegister = isRegister
                    _state.update { it.copy(isLoading = false, showConflictDialog = true) }
                } else {
                    _state.update { it.copy(isLoading = false, error = result.error) }
                }
            }
        }
    }

    private fun handleResolveConflict() {
        if (pendingEmail.isBlank()) return

        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isLoading = true, showConflictDialog = false) }

            // On relance avec forceSwitch = true
            val result = if (pendingIsRegister) {
                registerUseCase(pendingEmail, pendingPassword, forceSwitch = true)
            } else {
                loginUseCase(pendingEmail, pendingPassword, forceSwitch = true)
            }

            processAuthResult(result, pendingEmail, pendingPassword, pendingIsRegister)
        }
    }

    private fun handleCancelConflict() {
        clearPendingAuth()
        _state.update { it.copy(showConflictDialog = false) }
    }

    private fun handleLogout() {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            logoutUseCase(displayLocalData = false)
        }
    }

    private fun clearPendingAuth() {
        pendingEmail = ""
        pendingPassword = ""
    }
}