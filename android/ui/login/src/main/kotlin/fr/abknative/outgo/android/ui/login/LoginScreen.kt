package fr.abknative.outgo.android.ui.login

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.abknative.outgo.android.core.LoginLabels
import fr.abknative.outgo.android.ui.login.components.AuthLayout
import fr.abknative.outgo.android.ui.login.components.EmailAuthForm
import fr.abknative.outgo.android.ui.login.components.HandleLoginSideEffects
import fr.abknative.outgo.android.ui.login.components.handleGoogleSignIn
import fr.abknative.outgo.core.api.SecretConfig
import fr.abknative.outgo.login.api.LoginIntent
import fr.abknative.outgo.login.api.LoginPresenter
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun LoginScreen(
    presenter: LoginPresenter,
    onError: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val state by presenter.state.collectAsStateWithLifecycle()
    val secretConfig = koinInject<SecretConfig>()
    var isLoginMode by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        presenter.onIntent(LoginIntent.Init(isDeletionMode = false))
    }

    HandleLoginSideEffects(presenter, onError, onLoginSuccess)

    AuthLayout(
        title = LoginLabels.TITLE,
        isLoading = state.isLoading,
        onBackClick = onNavigateBack,
        onGoogleClick = {
            coroutineScope.launch {
                handleGoogleSignIn(context, secretConfig.webClientId, presenter, onError)
            }
        },
        onAppleClick = { /* TODO */ }
    ) {
        EmailAuthForm(
            emailInput = state.emailInput,
            passwordInput = state.passwordInput,
            isFormValid = state.isFormValid,
            isLoading = state.isLoading,
            isLoginMode = isLoginMode,
            onEmailChange = { presenter.onIntent(LoginIntent.UpdateEmail(it)) },
            onPasswordChange = { presenter.onIntent(LoginIntent.UpdatePassword(it)) },
            onSubmit = {
                if (isLoginMode) presenter.onIntent(LoginIntent.SubmitLogin)
                else presenter.onIntent(LoginIntent.SubmitRegister)
            },
            onToggleMode = { isLoginMode = !isLoginMode }
        )
    }
}