package fr.abknative.outgo.android.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.ui.LoginLabels
import fr.abknative.outgo.android.ui.states.rememberLoginFormState
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor
import fr.abknative.outgo.login.api.LoginIntent
import fr.abknative.outgo.login.api.LoginPresenter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    presenter: LoginPresenter,
    onNavigateBack: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val state by presenter.state.collectAsState()
    val formState = rememberLoginFormState()

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = AppTheme.colors.primary.toColor(),
        unfocusedBorderColor = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.2f),
        focusedLabelColor = AppTheme.colors.primary.toColor(),
        unfocusedLabelColor = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.6f),
        cursorColor = AppTheme.colors.primary.toColor(),
        focusedTextColor = AppTheme.colors.textPrimary.toColor(),
        unfocusedTextColor = AppTheme.colors.textPrimary.toColor()
    )

    LaunchedEffect(state.session) {
        if (state.session != null) onLoginSuccess()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(LoginLabels.BACK_TITLE) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(R.drawable.caret_left),
                            contentDescription = LoginLabels.BACK_TITLE
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.colors.background.toColor()
                )
            )
        },
        containerColor = AppTheme.colors.background.toColor()
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
                onClick = {
                    presenter.onIntent(LoginIntent.SubmitLogin(formState.email, formState.password))
                },
                enabled = formState.isValid && !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = AppTheme.colors.surface100.toColor())
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
                    text = "Pas encore de compte ? S'inscrire", // todo extraire
                    color = AppTheme.colors.primary.toColor()
                )
            }

            if (state.error != null) {
                Spacer(modifier = Modifier.height(AppTheme.spacing.small))
                Text(text = LoginLabels.ERROR_MESSAGE, color = AppTheme.colors.error.toColor())
            }
        }
    }
}