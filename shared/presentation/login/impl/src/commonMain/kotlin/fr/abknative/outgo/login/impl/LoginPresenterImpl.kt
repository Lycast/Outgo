package fr.abknative.outgo.login.impl

import androidx.lifecycle.viewModelScope
import fr.abknative.outgo.auth.api.AuthError
import fr.abknative.outgo.auth.api.usecase.*
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
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val loginWithAppleUseCase: LoginWithAppleUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val deleteFirebaseAuthUseCase: DeleteFirebaseAuthUseCase,
    private val observeUserSession: ObserveUserSessionUseCase
) : LoginPresenter() {

    private val _state = MutableStateFlow(LoginState(isLoading = false))
    override val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _events = Channel<LoginEvent>(Channel.BUFFERED)
    override val events = _events.receiveAsFlow()

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
            is LoginIntent.Init -> _state.update { it.copy(isDeletionMode = intent.isDeletionMode) }
            is LoginIntent.SubmitRegister -> handleRegister()
            is LoginIntent.SubmitLogin -> handleLogin()
            is LoginIntent.LoginWithGoogle -> handleGoogleLogin(intent.idToken)
            is LoginIntent.LoginWithApple -> handleAppleLogin(intent.idToken)
            is LoginIntent.Logout -> handleLogout()
            is LoginIntent.DismissError -> _state.update { it.copy(error = null) }
        }
    }

    private fun handleRegister() {
        val currentState = _state.value
        if (!currentState.isFormValid) return

        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = registerUseCase(currentState.emailInput, currentState.passwordInput)
            processAuthResult(result)
        }
    }

    private fun handleLogin() {
        val currentState = _state.value
        if (!currentState.isFormValid) return

        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = loginUseCase(
                email = currentState.emailInput,
                password = currentState.passwordInput,
                bypassMigration = currentState.isDeletionMode
            )
            processAuthResult(result)
        }
    }

    private fun handleGoogleLogin(idToken: String) {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = loginWithGoogleUseCase(idToken, bypassMigration = _state.value.isDeletionMode)
            processAuthResult(result)
        }
    }

    private fun handleAppleLogin(idToken: String) {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = loginWithAppleUseCase(idToken, bypassMigration = _state.value.isDeletionMode)
            processAuthResult(result)
        }
    }

    private fun processAuthResult(result: Result<Unit, AppException>) {
        when (result) {
            is Result.Success -> {
                if (_state.value.isDeletionMode) {
                    viewModelScope.safeLaunch(onError = onCoroutineError) {
                        when (val deleteResult = deleteFirebaseAuthUseCase()) {
                            is Result.Success -> {
                                _state.update { it.copy(isLoading = false, error = null, passwordInput = "") }
                                _events.trySend(LoginEvent.NavigateBack)
                            }
                            is Result.Error -> {
                                logoutUseCase(displayLocalData = true)
                                _state.update { it.copy(isLoading = false, error = deleteResult.error) }
                            }
                        }
                    }
                } else {
                    _state.update { it.copy(isLoading = false, error = null, passwordInput = "") }
                    _events.trySend(LoginEvent.NavigateBack)
                }
            }
            is Result.Error -> {
                if (result.error is AuthError.DataConflict) {
                    _state.update { it.copy(isLoading = false) }
                } else {
                    _state.update { it.copy(isLoading = false, error = result.error) }
                }
            }
        }
    }


    private fun handleLogout() {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            logoutUseCase(displayLocalData = false)
        }
    }
}