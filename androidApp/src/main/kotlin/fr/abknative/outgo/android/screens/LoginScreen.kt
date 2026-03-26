package fr.abknative.outgo.android.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.ui.states.rememberLoginFormState
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor
import fr.abknative.outgo.auth.api.presenter.AuthIntent
import fr.abknative.outgo.auth.api.presenter.AuthPresenter

@Composable
fun LoginScreen(
    presenter: AuthPresenter,
    onNavigateBack: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val state by presenter.state.collectAsState()
    val formState = rememberLoginFormState()

    LaunchedEffect(state.session) {
        if (state.session != null) onLoginSuccess()
    }

    Scaffold(containerColor = AppTheme.colors.background.toColor()) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(AppTheme.spacing.large),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenue sur Outgo",
                style = AppTheme.typo.title,
                color = AppTheme.colors.primary.toColor()
            )

            Spacer(modifier = Modifier.height(AppTheme.spacing.large))

            OutlinedTextField(
                value = formState.email,
                onValueChange = { formState.email = it },
                label = { Text("Email (debug@mail.fr)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(AppTheme.spacing.small))

            OutlinedTextField(
                value = formState.password,
                onValueChange = { formState.password = it },
                label = { Text("Mot de passe (debug)") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(AppTheme.spacing.extraLarge))

            Button(
                onClick = {
                    presenter.onIntent(AuthIntent.SubmitLogin(formState.email, formState.password))
                },
                enabled = formState.isValid && !state.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = AppTheme.colors.surface100.toColor())
                } else {
                    Text("Se connecter")
                }
            }

            if (state.error != null) {
                Spacer(modifier = Modifier.height(AppTheme.spacing.small))
                Text(text = "Erreur de connexion", color = AppTheme.colors.error.toColor())
            }
        }
    }
}