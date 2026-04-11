package fr.abknative.outgo.android.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.components.login.ConflictDialog
import fr.abknative.outgo.android.designsystem.components.buttons.AppButton
import fr.abknative.outgo.android.designsystem.components.buttons.AppOutlinedButton
import fr.abknative.outgo.android.designsystem.components.feedback.AppSnackbar
import fr.abknative.outgo.android.designsystem.components.inputs.AppTextField
import fr.abknative.outgo.android.designsystem.foundation.AppBackground
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.OutgoTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.LoginLabels
import fr.abknative.outgo.android.ui.states.rememberLoginFormState
import fr.abknative.outgo.android.ui.toUIString
import fr.abknative.outgo.login.api.LoginIntent
import fr.abknative.outgo.login.api.LoginPresenter
import fr.abknative.outgo.login.api.LoginState
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    presenter: LoginPresenter,
    onNavigateBack: () -> Unit,
    onLoginSuccess: () -> Unit
) {

    val state by presenter.state.collectAsStateWithLifecycle()
    val formState = rememberLoginFormState()

    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage = state.error?.toUIString()

    BackHandler(enabled = state.isLoading) {}

    LaunchedEffect(state.isLoginSuccessful) {
        if (state.isLoginSuccessful) {
            onLoginSuccess()
        }
    }

    LaunchedEffect(state.error) {
        if (state.error != null && errorMessage != null) {
            snackbarHostState.showSnackbar(message = errorMessage, withDismissAction = true)
            presenter.onIntent(LoginIntent.DismissError)
        }
    }

    AppBackground {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(
                        text = LoginLabels.BACK_TITLE,
                        style = AppTheme.typo.title.copy(fontWeight = FontWeight.Medium),
                        color = AppTheme.colors.textSecondary.toColor()
                    ) },
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
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(AppTheme.dimens.large),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = LoginLabels.TITLE,
                        style = AppTheme.typo.title,
                        color = AppTheme.colors.primary.toColor()
                    )

                    Spacer(modifier = Modifier.height(AppTheme.dimens.large))

                    AppTextField(
                        value = formState.email,
                        onValueChange = { formState.email = it },
                        label = LoginLabels.EMAIL_LABEL,
                        placeholder = "", // Ajout d'un placeholder vide ou d'un label adéquat
                        enabled = !state.isLoading
                    )

                    Spacer(modifier = Modifier.height(AppTheme.dimens.small))

                    AppTextField(
                        value = formState.password,
                        onValueChange = { formState.password = it },
                        label = LoginLabels.PASSWORD_LABEL,
                        placeholder = "",
                        visualTransformation = PasswordVisualTransformation(),
                        enabled = !state.isLoading
                    )

                    Spacer(modifier = Modifier.height(AppTheme.dimens.extraLarge))

                    AppButton(
                        onClick = {
                            presenter.onIntent(LoginIntent.SubmitLogin(formState.email, formState.password))
                        },
                        enabled = formState.isValid && !state.isLoading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = AppTheme.colors.textOnBrand.toColor(),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(LoginLabels.SUBMIT_BUTTON)
                        }
                    }

                    Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

                    AppOutlinedButton(
                        onClick = {
                            presenter.onIntent(LoginIntent.SubmitRegister(formState.email, formState.password))
                        },
                        enabled = formState.isValid && !state.isLoading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = LoginLabels.REGISTER_ACTION)
                    }
                }
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.align(Alignment.TopCenter)
                ) { data ->
                    AppSnackbar(data)
                }
            }
        }

        // --- NOUVEAU : LA POPUP DE RÉSOLUTION DE CONFLIT ---
        if (state.showConflictDialog) {
            ConflictDialog(
                onConfirm = {
                    presenter.onIntent(LoginIntent.ResolveConflict)
                },
                onCancel = {
                    presenter.onIntent(LoginIntent.CancelConflict)
                }
            )
        }
    }
}

/**
 * Preview for the LoginScreen.
 */
@Preview(showBackground = true, name = "Login Screen - Default")
@Preview(showBackground = true, name = "Login Screen - Default", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewLoginScreen() {
    val dummyPresenter = object : LoginPresenter() {
        override val state = MutableStateFlow(
            LoginState(
                isLoading = false,
                error = null,
                session = null,
                showConflictDialog = false
            )
        )
        override fun onIntent(intent: LoginIntent) {}
    }

    OutgoTheme {
        LoginScreen(
            presenter = dummyPresenter,
            onNavigateBack = {},
            onLoginSuccess = {}
        )
    }
}