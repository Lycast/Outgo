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
            is LoginIntent.UpdateEmail -> _state.update { it.copy(emailInput = intent.email) }
            is LoginIntent.UpdatePassword -> _state.update { it.copy(passwordInput = intent.password) }
            is LoginIntent.SubmitRegister -> handleRegister()
            is LoginIntent.SubmitLogin -> handleLogin()
            is LoginIntent.LoginWithGoogle, LoginIntent.LoginWithApple -> { /* Nothing yet */ }
            is LoginIntent.Logout -> handleLogout()
            is LoginIntent.DismissError -> _state.update { it.copy(error = null) }
            is LoginIntent.ResolveConflict -> handleResolveConflict()
            is LoginIntent.CancelConflict -> _state.update { it.copy(showConflictDialog = false) }
        }
    }

    private fun handleRegister() {
        val currentState = _state.value
        if (!currentState.isFormValid) return

        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isLoading = true, error = null, showConflictDialog = false) }
            val result = registerUseCase(currentState.emailInput, currentState.passwordInput)
            processAuthResult(result, isRegister = true)
        }
    }

    private fun handleLogin() {
        val currentState = _state.value
        if (!currentState.isFormValid) return

        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isLoading = true, error = null, showConflictDialog = false) }
            val result = loginUseCase(currentState.emailInput, currentState.passwordInput)
            processAuthResult(result, isRegister = false)
        }
    }

    private fun processAuthResult(
        result: Result<Unit, AppException>,
        isRegister: Boolean
    ) {
        when (result) {
            is Result.Success -> {
                _state.update { it.copy(isLoading = false, error = null, passwordInput = "") }
                _events.trySend(LoginEvent.NavigateBack)
            }
            is Result.Error -> {
                if (result.error is AuthError.DataConflict) {
                    pendingIsRegister = isRegister
                    _state.update { it.copy(isLoading = false, showConflictDialog = true) }
                } else {
                    _state.update { it.copy(isLoading = false, error = result.error) }
                }
            }
        }
    }

    private fun handleResolveConflict() {
        val currentState = _state.value
        if (!currentState.isFormValid) return

        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isLoading = true, showConflictDialog = false) }

            val result = if (pendingIsRegister) {
                registerUseCase(currentState.emailInput, currentState.passwordInput, forceSwitch = true)
            } else {
                loginUseCase(currentState.emailInput, currentState.passwordInput, forceSwitch = true)
            }

            processAuthResult(result, pendingIsRegister)
        }
    }

    private fun handleLogout() {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            logoutUseCase(displayLocalData = false)
        }
    }
}