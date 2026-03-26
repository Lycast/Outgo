package fr.abknative.outgo.auth.impl.presenter

import androidx.lifecycle.viewModelScope
import fr.abknative.outgo.auth.api.presenter.AuthIntent
import fr.abknative.outgo.auth.api.presenter.AuthPresenter
import fr.abknative.outgo.auth.api.presenter.AuthState
import fr.abknative.outgo.auth.api.usecase.LoginUseCase
import fr.abknative.outgo.auth.api.usecase.LogoutUseCase
import fr.abknative.outgo.auth.api.usecase.ObserveUserSessionUseCase
import fr.abknative.outgo.core.api.extensions.safeLaunch
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class AuthPresenterImpl(
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val observeUserSession: ObserveUserSessionUseCase
) : AuthPresenter() {

    private val _state = MutableStateFlow(AuthState(isLoading = true))
    override val state: StateFlow<AuthState> = _state.asStateFlow()

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

    override fun onIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.SubmitLogin -> handleLogin(intent.email, intent.password)
            is AuthIntent.LoginWithGoogle, AuthIntent.LoginWithApple -> { /* Rien pour l'instant */ }
            AuthIntent.Logout -> handleLogout()
            AuthIntent.DismissError -> _state.update { it.copy(error = null) }
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