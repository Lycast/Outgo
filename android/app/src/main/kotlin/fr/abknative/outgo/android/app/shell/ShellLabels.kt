package fr.abknative.outgo.android.app.shell

import androidx.compose.runtime.Composable
import fr.abknative.outgo.shared.core.ui.resources.*
import org.jetbrains.compose.resources.stringResource

internal object ShellLabels {

    val SYNC_PROMO_TITLE @Composable get() = stringResource(Res.string.shell_header_sync_promo_title)
    val SYNC_PROMO_DESC @Composable get() = stringResource(Res.string.shell_header_sync_promo_desc)
    val SYNC_PROMO_ACTION_LOGIN @Composable get() = stringResource(Res.string.shell_header_sync_promo_action_login)
    val SYNC_PROMO_ACTION_LATER @Composable get() = stringResource(Res.string.shell_header_sync_promo_action_later)

    val SYNC_OFFLINE_TITLE @Composable get() = stringResource(Res.string.shell_sync_offline_title)
    val SYNC_OFFLINE_DESC @Composable get() = stringResource(Res.string.shell_sync_offline_desc)

    val SYNC_LOADING_TITLE @Composable get() = stringResource(Res.string.shell_dialog_sync_title)
    val SYNC_LOADING_MESSAGE @Composable get() = stringResource(Res.string.shell_dialog_sync_message)

    val SYNC_ERROR_TITLE @Composable get() = stringResource(Res.string.shell_sync_error_title)
    val SYNC_ERROR_MESSAGE @Composable get() = stringResource(Res.string.shell_sync_error_message)

    val CONFLICT_TITLE @Composable get() = stringResource(Res.string.shell_conflict_title)
    val CONFLICT_DESC @Composable get() = stringResource(Res.string.shell_conflict_desc)
    val CONFLICT_QUESTION @Composable get() = stringResource(Res.string.shell_conflict_question)
    val CONFLICT_CONFIRM @Composable get() = stringResource(Res.string.shell_conflict_confirm)
    val CONFLICT_CANCEL @Composable get() = stringResource(Res.string.shell_conflict_cancel)

    val EMAIL_VERIFICATION_MESSAGE @Composable get() = stringResource(Res.string.shell_email_verification_message)
    val EMAIL_VERIFICATION_ACTION @Composable get() = stringResource(Res.string.shell_email_verification_action)
    val EMAIL_NOT_VERIFIED_MESSAGE @Composable get() = stringResource(Res.string.shell_email_not_ferified_message)
}