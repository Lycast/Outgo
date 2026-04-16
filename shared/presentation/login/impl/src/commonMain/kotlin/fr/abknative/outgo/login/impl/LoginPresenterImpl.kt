package fr.abknative.outgo.login.impl

import androidx.lifecycle.viewModelScope
import fr.abknative.outgo.auth.api.AuthError
import fr.abknative.outgo.auth.api.usecase.*
import fr.abknative.outgo.core.api.extensions.safeLaunch
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.login.api.*
import fr.abknative.outgo.sync.api.SyncManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

internal class LoginPresenterImpl(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val loginWithAppleUseCase: LoginWithAppleUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val observeUserSession: ObserveUserSessionUseCase,
    private val syncManager: SyncManager
) : LoginPresenter() {

    private sealed interface PendingAuth {
        data object Register : PendingAuth
        data object Login : PendingAuth
        data class Google(val idToken: String) : PendingAuth
        data class Apple(val idToken: String) : PendingAuth
    }

    private var pendingAuthAction: PendingAuth? = null

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
            is LoginIntent.SubmitRegister -> handleRegister()
            is LoginIntent.SubmitLogin -> handleLogin()
            is LoginIntent.LoginWithGoogle -> handleGoogleLogin(intent.idToken)
            is LoginIntent.LoginWithApple -> handleAppleLogin(intent.idToken)
            is LoginIntent.Logout -> handleLogout()
            is LoginIntent.DismissError -> _state.update { it.copy(error = null) }
            is LoginIntent.ResolveConflict -> handleResolveConflict()
            is LoginIntent.CancelConflict -> handleCancelConflict()
            is LoginIntent.RetrySync -> handleStartSync()
        }
    }

    private fun handleRegister() {
        val currentState = _state.value
        if (!currentState.isFormValid) return

        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isLoading = true, error = null, showConflictDialog = false) }
            val result = registerUseCase(currentState.emailInput, currentState.passwordInput)
            processAuthResult(result, PendingAuth.Register)
        }
    }

    private fun handleLogin() {
        val currentState = _state.value
        if (!currentState.isFormValid) return

        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isLoading = true, error = null, showConflictDialog = false) }
            val result = loginUseCase(currentState.emailInput, currentState.passwordInput)
            processAuthResult(result, PendingAuth.Login)
        }
    }

    // --- NOUVEAU : Gestion Google ---
    private fun handleGoogleLogin(idToken: String) {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isLoading = true, error = null, showConflictDialog = false) }
            val result = loginWithGoogleUseCase(idToken, forceSwitch = false)
            processAuthResult(result, PendingAuth.Google(idToken))
        }
    }

    // --- NOUVEAU : Gestion Apple ---
    private fun handleAppleLogin(idToken: String) {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isLoading = true, error = null, showConflictDialog = false) }
            val result = loginWithAppleUseCase(idToken, forceSwitch = false)
            processAuthResult(result, PendingAuth.Apple(idToken))
        }
    }

    private fun processAuthResult(
        result: Result<Unit, AppException>,
        currentAuthAction: PendingAuth
    ) {
        when (result) {
            is Result.Success -> {
                _state.update { it.copy(isLoading = false, error = null, passwordInput = "") }
                pendingAuthAction = null // On nettoie
                handleStartSync()
            }
            is Result.Error -> {
                if (result.error is AuthError.DataConflict) {
                    pendingAuthAction = currentAuthAction
                    _state.update { it.copy(isLoading = false, postLoginStep = PostLoginStep.CONFLICT) }
                } else {
                    _state.update { it.copy(isLoading = false, error = result.error) }
                }
            }
        }
    }

    private fun handleResolveConflict() {
        val actionToRetry = pendingAuthAction ?: return // Sécurité
        val currentState = _state.value

        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(isLoading = true, postLoginStep = PostLoginStep.NONE) }

            // 👈 On relance la bonne action avec forceSwitch = true
            val result = when (actionToRetry) {
                is PendingAuth.Register -> registerUseCase(currentState.emailInput, currentState.passwordInput, forceSwitch = true)
                is PendingAuth.Login -> loginUseCase(currentState.emailInput, currentState.passwordInput, forceSwitch = true)
                is PendingAuth.Google -> loginWithGoogleUseCase(actionToRetry.idToken, forceSwitch = true)
                is PendingAuth.Apple -> loginWithAppleUseCase(actionToRetry.idToken, forceSwitch = true)
            }

            processAuthResult(result, actionToRetry)
        }
    }

    private fun handleCancelConflict() {
        val wasInError = _state.value.postLoginStep == PostLoginStep.ERROR

        _state.update { it.copy(postLoginStep = PostLoginStep.NONE) }

        if (wasInError) { handleLogout() }
    }

    private fun handleStartSync() {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            _state.update { it.copy(postLoginStep = PostLoginStep.SYNCING, syncError = null) }

            when (val syncResult = syncManager.syncAll()) {
                is Result.Success -> {
                    _state.update { it.copy(postLoginStep = PostLoginStep.NONE) }
                    _events.trySend(LoginEvent.NavigateBack)
                }
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            postLoginStep = PostLoginStep.ERROR,
                            syncError = syncResult.error
                        )
                    }
                }
            }
        }
    }

    private fun handleLogout() {
        viewModelScope.safeLaunch(onError = onCoroutineError) {
            logoutUseCase(displayLocalData = false)
            syncManager.clearSyncState()
        }
    }
}