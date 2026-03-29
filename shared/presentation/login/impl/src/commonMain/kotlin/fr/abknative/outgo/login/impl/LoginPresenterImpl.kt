package fr.abknative.outgo.login.impl

import androidx.lifecycle.viewModelScope
import fr.abknative.outgo.auth.api.usecase.LoginUseCase
import fr.abknative.outgo.auth.api.usecase.LogoutUseCase
import fr.abknative.outgo.auth.api.usecase.ObserveUserSessionUseCase
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
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val observeUserSession: ObserveUserSessionUseCase
) : LoginPresenter() {

    private val _state = MutableStateFlow(LoginState(isLoading = true))
    override val state: StateFlow<LoginState> = _state.asStateFlow()

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
            is LoginIntent.SubmitLogin -> handleLogin(intent.email, intent.password)
            is LoginIntent.LoginWithGoogle, LoginIntent.LoginWithApple -> { /* Rien pour l'instant */ }
            LoginIntent.Logout -> handleLogout()
            LoginIntent.DismissError -> _state.update { it.copy(error = null) }
        }
    }

    private fun handleLogin(email: String, password: String) {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = loginUseCase(email, password)

            when (result) {
                is Result.Success -> {
                    _state.update { it.copy(isLoading = false, error = null) }
                }
                is Result.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.error) }
                }
            }
        }
    }

    private fun handleLogout() {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            logoutUseCase()
        }
    }
}