package fr.abknative.outgo.login.impl

import androidx.lifecycle.viewModelScope
import fr.abknative.outgo.auth.api.AuthError
import fr.abknative.outgo.auth.api.model.ConflictStrategy
import fr.abknative.outgo.auth.api.usecase.LoginUseCase
import fr.abknative.outgo.auth.api.usecase.LogoutUseCase
import fr.abknative.outgo.auth.api.usecase.ObserveUserSessionUseCase
import fr.abknative.outgo.auth.api.usecase.RegisterUseCase
import fr.abknative.outgo.core.api.extensions.safeLaunch
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.login.api.LoginIntent
import fr.abknative.outgo.login.api.LoginPresenter
import fr.abknative.outgo.login.api.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class LoginPresenterImpl(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val observeUserSession: ObserveUserSessionUseCase
) : LoginPresenter() {

    private val _state = MutableStateFlow(LoginState(isLoading = true))
    override val state: StateFlow<LoginState> = _state.asStateFlow()

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
                _state.update { it.copy(session = session, isLoading = false) }
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
            is LoginIntent.ResolveConflict -> handleResolveConflict(intent.strategy)
            is LoginIntent.CancelConflict -> handleCancelConflict()
        }
    }

    private fun handleRegister(email: String, password: String) {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isLoading = true, error = null, showConflictDialog = false) }
            val result = registerUseCase(email, password)
            processAuthResult(result, email, password, isRegister = true)
        }
    }

    private fun handleLogin(email: String, password: String) {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isLoading = true, error = null, showConflictDialog = false) }
            val result = loginUseCase(email, password)
            processAuthResult(result, email, password, isRegister = false)
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
                _state.update { it.copy(isLoading = false, error = null) }
                clearPendingAuth()
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

    /**
     * Called when the user has selected a strategy from the popup.
     * Restarts the initial action (Login or Register) by enforcing the chosen strategy.
     */
    private fun handleResolveConflict(strategy: ConflictStrategy) {
        if (pendingEmail.isBlank()) return // Sécurité

        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isLoading = true, showConflictDialog = false) }

            val result = if (pendingIsRegister) {
                registerUseCase(pendingEmail, pendingPassword, strategy)
            } else {
                loginUseCase(pendingEmail, pendingPassword, strategy)
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
            logoutUseCase()
        }
    }

    private fun clearPendingAuth() {
        pendingEmail = ""
        pendingPassword = ""
    }
}