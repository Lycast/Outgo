package fr.abknative.outgo.android.ui.login

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor
import fr.abknative.outgo.android.ui.login.components.*
import fr.abknative.outgo.core.api.SecretConfig
import fr.abknative.outgo.login.api.LoginIntent
import fr.abknative.outgo.login.api.LoginPresenter
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun DeleteAccountScreen(
    presenter: LoginPresenter,
    onError: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onDeleteSuccess: () -> Unit
) {
    val state by presenter.state.collectAsStateWithLifecycle()
    val secretConfig = koinInject<SecretConfig>()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var showCancelDialog by remember { mutableStateOf(false) }
    val googleErrorMessage = LoginLabels.GOOGLE_AUTH_ERROR // todo l'erreur n'est pas géré ici

    LaunchedEffect(Unit) { presenter.onIntent(LoginIntent.Init(isDeletionMode = true)) }

    HandleLoginSideEffects(presenter, onError, onDeleteSuccess)

    BackHandler(enabled = !state.isLoading) { showCancelDialog = true }

    AuthLayout(
        title = LoginLabels.DELETE_TITLE,
        subtitle = LoginLabels.DELETE_SUBTITLE,
        titleColor = AppTheme.colors.error.toColor().copy(alpha = 0.5f),
        googleLabel = LoginLabels.DELETE_GOOGLE_BUTTON,
        appleLabel = LoginLabels.DELETE_APPLE_BUTTON,
        isLoading = state.isLoading,
        onBackClick = { showCancelDialog = true },
        onGoogleClick = {
            coroutineScope.launch {
                handleGoogleSignIn(context, secretConfig.webClientId, presenter, onError, errorMessage = googleErrorMessage)
            }
        },
        onAppleClick = { /* TODO */ }
    ) {
        EmailAuthForm(
            emailInput = state.emailInput,
            passwordInput = state.passwordInput,
            isFormValid = state.isFormValid,
            isLoading = state.isLoading,
            isLoginMode = true,
            submitBtnLabel = LoginLabels.DELETE_SUBMIT,
            showToggleMode = false,
            onEmailChange = { presenter.onIntent(LoginIntent.UpdateEmail(it)) },
            onPasswordChange = { presenter.onIntent(LoginIntent.UpdatePassword(it)) },
            onSubmit = { presenter.onIntent(LoginIntent.SubmitLogin) },
            onToggleMode = {}
        )
    }

    if (showCancelDialog) {
        CancelDeletionDialog(onDismiss = { showCancelDialog = false }, onConfirmQuit = onNavigateBack)
    }
}