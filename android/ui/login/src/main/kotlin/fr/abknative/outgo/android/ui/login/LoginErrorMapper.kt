package fr.abknative.outgo.android.ui.login

import androidx.compose.runtime.Composable
import fr.abknative.outgo.android.core.toCommonUIString
import fr.abknative.outgo.auth.api.AuthError
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.shared.core.ui.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppException.toLoginUIString(): String {
    return when (this) {

        is AuthError.InvalidCredentials -> stringResource(Res.string.error_auth_invalid_credentials)
        is AuthError.UserNotFound -> stringResource(Res.string.error_auth_user_not_found)
        is AuthError.SessionExpired -> stringResource(Res.string.error_auth_session_expired)
        is AuthError.NeedsReauthentication -> stringResource(Res.string.error_auth_needs_reauth)
        is AuthError.DataConflict -> stringResource(Res.string.error_auth_data_conflict)
        is AuthError.SystemError -> stringResource(Res.string.error_credential_system)

        else -> this.toCommonUIString()
    }
}