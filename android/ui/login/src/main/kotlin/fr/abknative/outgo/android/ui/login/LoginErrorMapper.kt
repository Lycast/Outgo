package fr.abknative.outgo.android.ui.login

import androidx.compose.runtime.Composable
import fr.abknative.outgo.android.ui.login.helper.CredentialErrorType
import fr.abknative.outgo.auth.api.AuthError
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.shared.core.ui.resources.*
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppException.toUIString(): String {
    return when (this) {

        // --- Domaine : Authentification ---
        is AuthError.InvalidCredentials -> stringResource(Res.string.error_auth_invalid_credentials)
        is AuthError.UserNotFound -> stringResource(Res.string.error_auth_user_not_found)
        is AuthError.SessionExpired -> stringResource(Res.string.error_auth_session_expired)
        is AuthError.NeedsReauthentication -> stringResource(Res.string.error_auth_needs_reauth)
        is AuthError.DataConflict -> stringResource(Res.string.error_auth_data_conflict)


        // --- Sécurité (Fallback) ---
        else -> stringResource(Res.string.error_global_unknown)
    }
}

suspend fun CredentialErrorType.resolveUIString(): String {
    return when (this) {
        CredentialErrorType.INVALID_TOKEN -> getString(Res.string.error_credential_invalid_token)
        CredentialErrorType.NO_ACCOUNT_FOUND -> getString(Res.string.error_credential_no_account)
        CredentialErrorType.SYSTEM_ERROR -> getString(Res.string.error_credential_system)
        CredentialErrorType.UNKNOWN -> getString(Res.string.error_global_unknown)
    }
}