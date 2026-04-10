package fr.abknative.outgo.android.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.components.login.ConflictDialog
import fr.abknative.outgo.android.components.shell.AppBackground
import fr.abknative.outgo.android.ui.LoginLabels
import fr.abknative.outgo.android.ui.states.rememberLoginFormState
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.OutgoTheme
import fr.abknative.outgo.android.ui.theme.toColor
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
    val state by presenter.state.collectAsState()
    val formState = rememberLoginFormState()
    BackHandler(enabled = state.isLoading) {}

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = AppTheme.colors.primary.toColor(),
        unfocusedBorderColor = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.2f),
        focusedLabelColor = AppTheme.colors.primary.toColor(),
        unfocusedLabelColor = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.6f),
        cursorColor = AppTheme.colors.primary.toColor(),
        focusedTextColor = AppTheme.colors.textPrimary.toColor(),
        unfocusedTextColor = AppTheme.colors.textPrimary.toColor()
    )

    LaunchedEffect(state.isLoginSuccessful) {
        if (state.isLoginSuccessful) {
            onLoginSuccess()
        }
    }

    AppBackground {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(
                        LoginLabels.BACK_TITLE,
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(AppTheme.spacing.large),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = LoginLabels.TITLE,
                    style = AppTheme.typo.title,
                    color = AppTheme.colors.primary.toColor()
                )

                Spacer(modifier = Modifier.height(AppTheme.spacing.large))

                OutlinedTextField(
                    value = formState.email,
                    onValueChange = { formState.email = it },
                    label = { Text(LoginLabels.EMAIL_LABEL) },
                    colors = textFieldColors,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(AppTheme.spacing.small))

                OutlinedTextField(
                    value = formState.password,
                    onValueChange = { formState.password = it },
                    label = { Text(LoginLabels.PASSWORD_LABEL) },
                    colors = textFieldColors,
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(AppTheme.spacing.extraLarge))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        presenter.onIntent(LoginIntent.SubmitLogin(formState.email, formState.password))
                    },
                    enabled = formState.isValid && !state.isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppTheme.colors.primary.toColor().copy(alpha = 0.8f),
                        contentColor = AppTheme.colors.textOnBrand.toColor(),
                        disabledContainerColor = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.2f),
                        disabledContentColor = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.8f)
                    )
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = AppTheme.colors.surface100.toColor()
                        )
                    } else {
                        Text(LoginLabels.SUBMIT_BUTTON)
                    }
                }

                Spacer(modifier = Modifier.height(AppTheme.spacing.medium))

                TextButton(
                    onClick = {
                        presenter.onIntent(LoginIntent.SubmitRegister(formState.email, formState.password))
                    },
                    enabled = formState.isValid && !state.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Pas encore de compte ? S'inscrire",
                        color = AppTheme.colors.primary.toColor()
                    )
                }

                if (state.error != null) {
                    Spacer(modifier = Modifier.height(AppTheme.spacing.small))
                    Text(text = LoginLabels.ERROR_MESSAGE, color = AppTheme.colors.error.toColor())
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