package fr.abknative.outgo.android.components.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.abknative.outgo.android.designsystem.components.buttons.AppOutlinedButton
import fr.abknative.outgo.android.designsystem.components.buttons.HoldToConfirmButton
import fr.abknative.outgo.android.designsystem.components.feedback.ConfirmationDialog
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.android.ui.DialogLabels
import fr.abknative.outgo.settings.api.SettingsIntent
import fr.abknative.outgo.settings.api.SettingsPresenter

@Composable
fun SettingsModals(
    showLogoutOptions: Boolean,
    showDeleteAccountConfirm: Boolean,
    showPurgeConfirm: Boolean,
    onDismissLogout: () -> Unit,
    onDismissDelete: () -> Unit,
    onDismissPurge: () -> Unit,
    presenter: SettingsPresenter
) {
    if (showLogoutOptions) {
        LogoutDialog(
            onKeepOffline = {
                presenter.onIntent(SettingsIntent.Logout(displayLocalData = true))
                onDismissLogout()
            },
            onReturnToLocal = {
                presenter.onIntent(SettingsIntent.Logout(displayLocalData = false))
                onDismissLogout()
            },
            onCancel = onDismissLogout
        )
    }

    if (showDeleteAccountConfirm) {
        DeleteAccountDialog(
            onConfirm = { wipeLocal, wipeServer, revokeAuth ->
                presenter.onIntent(SettingsIntent.DeleteAccount(wipeLocal, wipeServer, revokeAuth))
                onDismissDelete()
            },
            onDismiss = onDismissDelete
        )
    }

    if (showPurgeConfirm) {
        ConfirmationDialog(
            title = DialogLabels.PURGE_TITLE,
            description = DialogLabels.PURGE_DESC,
            onDismiss = onDismissPurge,
            confirmButton = {
                HoldToConfirmButton(
                    label = DialogLabels.PURGE_CONFIRM,
                    onConfirm = {
                        presenter.onIntent(SettingsIntent.PurgeLocalData)
                        onDismissPurge()
                    }
                )
            },
            dismissButton = {
                AppOutlinedButton(
                    onClick = onDismissPurge,
                    modifier = Modifier.padding(end = AppTheme.dimens.medium)
                ) { Text(text = CommonLabels.ACTION_CANCEL) }
            }
        )
    }
}