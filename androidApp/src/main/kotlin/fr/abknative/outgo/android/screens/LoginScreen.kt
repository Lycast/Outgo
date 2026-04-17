package fr.abknative.outgo.android.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.components.login.EmailAuthForm
import fr.abknative.outgo.android.components.login.PostLoginDialog
import fr.abknative.outgo.android.components.login.SocialLoginButton
import fr.abknative.outgo.android.components.login.SocialProvider
import fr.abknative.outgo.android.designsystem.components.feedback.AppSnackbar
import fr.abknative.outgo.android.designsystem.foundation.AppBackground
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.LoginLabels
import fr.abknative.outgo.android.ui.auth.CredentialErrorType
import fr.abknative.outgo.android.ui.auth.CredentialResult
import fr.abknative.outgo.android.ui.auth.launchGoogleSignIn
import fr.abknative.outgo.android.ui.resolveUIString
import fr.abknative.outgo.android.ui.toUIString
import fr.abknative.outgo.core.api.SecretConfig
import fr.abknative.outgo.login.api.LoginEvent
import fr.abknative.outgo.login.api.LoginIntent
import fr.abknative.outgo.login.api.LoginPresenter
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    presenter: LoginPresenter,
    onNavigateBack: () -> Unit,
    onLoginSuccess: () -> Unit
) {

    val state by presenter.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val secretConfig = koinInject<SecretConfig>()
    var isLoginMode by remember { mutableStateOf(true) }
    var googleAuthError by remember { mutableStateOf<CredentialErrorType?>(null) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    BackHandler(enabled = state.isLoading) {}

    LaunchedEffect(Unit) {
        presenter.events.collect { event ->
            when (event) {
                is LoginEvent.NavigateBack -> {
                    onLoginSuccess()
                }
            }
        }
    }

    val presenterError = state.error
    val presenterErrorMessage = presenterError?.toUIString()

    LaunchedEffect(presenterError) {
        if (presenterError != null && presenterErrorMessage != null) {
            snackbarHostState.showSnackbar(message = presenterErrorMessage, withDismissAction = true)
            presenter.onIntent(LoginIntent.DismissError)
        }
    }

    LaunchedEffect(googleAuthError) {
        val currentError = googleAuthError
        if (currentError != null) {
            val message = currentError.resolveUIString()
            snackbarHostState.showSnackbar(message = message, withDismissAction = true)
            googleAuthError = null
        }
    }

    AppBackground {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = LoginLabels.BACK_TITLE,
                            style = AppTheme.typo.title.copy(fontWeight = FontWeight.Medium),
                            color = AppTheme.colors.textSecondary.toColor(),
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            enabled = !state.isLoading,
                            onClick = onNavigateBack
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.caret_left),
                                contentDescription = LoginLabels.BACK_TITLE,
                                tint = AppTheme.colors.textSecondary.toColor()
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = AppTheme.dimens.large),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = LoginLabels.TITLE,
                        style = AppTheme.typo.subtitle.copy(fontWeight = FontWeight.Medium),
                        color = AppTheme.colors.primary.toColor(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(AppTheme.dimens.big))

                    // --- Zone des Boutons Sociaux ---
                    SocialLoginButton(
                        provider = SocialProvider.GOOGLE,
                        onClick = {
                            coroutineScope.launch {
                                when (val result = launchGoogleSignIn(context, secretConfig.webClientId)) {
                                    is CredentialResult.Success -> {
                                        presenter.onIntent(LoginIntent.LoginWithGoogle(result.idToken))
                                    }
                                    is CredentialResult.Error -> { googleAuthError = result.type }
                                    is CredentialResult.Cancelled -> { /* silencieux */ }
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(AppTheme.dimens.small))

                    SocialLoginButton(
                        provider = SocialProvider.APPLE,
                        onClick = { /* TODO: Implémenter Apple Login */ }
                    )

                    Spacer(modifier = Modifier.height(AppTheme.dimens.large))

                    // --- Séparateur visuel ---
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = AppTheme.colors.textSecondary.toColor()
                        )
                        Text(
                            text = LoginLabels.OR_LABEL,
                            color = AppTheme.colors.textSecondary.toColor(),
                            modifier = Modifier.padding(horizontal = AppTheme.dimens.small)
                        )
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = AppTheme.colors.textSecondary.toColor()
                        )
                    }

                    Spacer(modifier = Modifier.height(AppTheme.dimens.large))

                    // --- Zone Formulaire Email ---
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

                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.align(Alignment.TopCenter)
                ) { data ->
                    AppSnackbar(data)
                }
            }
        }

        PostLoginDialog(
            step = state.postLoginStep,
            errorMessage = state.syncError?.toUIString(),
            onResolveConflict = { presenter.onIntent(LoginIntent.ResolveConflict) },
            onCancel = { presenter.onIntent(LoginIntent.CancelConflict) },
            onRetry = { presenter.onIntent(LoginIntent.RetrySync) }
        )
    }
}