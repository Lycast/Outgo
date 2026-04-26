package fr.abknative.outgo.android.ui.login

import androidx.compose.runtime.Composable
import fr.abknative.outgo.shared.core.ui.resources.*
import org.jetbrains.compose.resources.stringResource

internal object LoginLabels {

    val TITLE @Composable get() = stringResource(Res.string.login_title)
    val EMAIL_LABEL @Composable get() = stringResource(Res.string.login_email_label)
    val PASSWORD_LABEL @Composable get() = stringResource(Res.string.login_password_label)
    val BACK_TITLE @Composable get() = stringResource(Res.string.login_back_title)

    val GOOGLE_BUTTON @Composable get() = stringResource(Res.string.login_google_button)
    val APPLE_BUTTON @Composable get() = stringResource(Res.string.login_apple_button)
    val SUBMIT_BUTTON @Composable get() = stringResource(Res.string.login_submit_button)
    val REGISTER_BUTTON @Composable get() = stringResource(Res.string.login_register_button)
    val SUBMIT_ACTION @Composable get() = stringResource(Res.string.login_submit_action)
    val REGISTER_ACTION @Composable get() = stringResource(Res.string.login_register_action)
    val OR_LABEL @Composable get() = stringResource(Res.string.login_or_label)

    val DELETE_TITLE @Composable get() = stringResource(Res.string.login_delete_security_title)
    val DELETE_SUBTITLE @Composable get() = stringResource(Res.string.login_delete_security_subtitle)
    val DELETE_SUBMIT @Composable get() = stringResource(Res.string.login_delete_submit_button)
    val DELETE_CANCEL_DIALOG_TITLE @Composable get() = stringResource(Res.string.login_delete_cancel_title)
    val DELETE_CANCEL_DIALOG_DESC @Composable get() = stringResource(Res.string.login_delete_cancel_desc)
    val DELETE_GOOGLE_BUTTON @Composable get() = stringResource(Res.string.login_delete_google_button)
    val DELETE_APPLE_BUTTON @Composable get() = stringResource(Res.string.login_delete_apple_button)

    val DELETE_CANCEL_QUIT @Composable get() = stringResource(Res.string.login_delete_cancel_quit)
    val DELETE_CANCEL_CONTINUE @Composable get() = stringResource(Res.string.login_delete_cancel_continue)
    val GOOGLE_AUTH_ERROR @Composable get() = stringResource(Res.string.login_error_google_auth)
}