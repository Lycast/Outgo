package fr.abknative.outgo.android.ui.login.components

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.abknative.outgo.android.ui.login.helper.CredentialResult
import fr.abknative.outgo.android.ui.login.helper.launchGoogleSignIn
import fr.abknative.outgo.android.ui.login.toLoginUIString
import fr.abknative.outgo.login.api.LoginEvent
import fr.abknative.outgo.login.api.LoginIntent
import fr.abknative.outgo.login.api.LoginPresenter


@Composable
fun HandleLoginSideEffects(
    presenter: LoginPresenter,
    onError: (String) -> Unit,
    onSuccess: () -> Unit,
) {
    val state by presenter.state.collectAsStateWithLifecycle()
    val presenterError = state.error
    val presenterErrorMessage = presenterError?.toLoginUIString()

    LaunchedEffect(Unit) {
        presenter.events.collect { event ->
            if (event is LoginEvent.NavigateBack) {
                onSuccess()
            }
        }
    }

    LaunchedEffect(presenterError) {
        if (presenterError != null && presenterErrorMessage != null) {
            onError(presenterErrorMessage)
            presenter.onIntent(LoginIntent.DismissError)
        }
    }
}


suspend fun handleGoogleSignIn(
    context: Context,
    webClientId: String,
    presenter: LoginPresenter,
    onError: (String) -> Unit,
    errorMessage: String
) {
    when (val result = launchGoogleSignIn(context, webClientId)) {
        is CredentialResult.Success -> {
            presenter.onIntent(LoginIntent.LoginWithGoogle(result.idToken))
        }
        is CredentialResult.Error -> {
            onError(errorMessage)
        }
        is CredentialResult.Cancelled -> { /* Rien à faire */ }
    }
}