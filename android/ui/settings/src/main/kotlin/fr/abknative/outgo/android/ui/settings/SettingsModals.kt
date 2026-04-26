package fr.abknative.outgo.android.ui.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.abknative.outgo.android.core.CommonLabels
import fr.abknative.outgo.android.core.components.buttons.AppOutlinedButton
import fr.abknative.outgo.android.core.components.buttons.HoldToConfirmButton
import fr.abknative.outgo.android.core.designsystem.AppText
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.ui.settings.components.DeleteAccountDialog
import fr.abknative.outgo.android.ui.settings.components.LocalPurgeDialog
import fr.abknative.outgo.android.ui.settings.components.LogoutDialog
import fr.abknative.outgo.settings.api.SettingsIntent
import fr.abknative.outgo.settings.api.SettingsPresenter

@Composable
fun SettingsModals(
    showLogoutOptions: Boolean,
    showDeleteAccountDialog: Boolean,
    showLocalPurgeDialog: Boolean,
    onDismissLogout: () -> Unit,
    onDismissDeleteAccount: () -> Unit,
    onDismissLocalPurge: () -> Unit,
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

    if (showDeleteAccountDialog) {
        DeleteAccountDialog(
            onConfirm = { wipeServer, revokeAuth ->
                presenter.onIntent(SettingsIntent.DeleteAccount(wipeServer, revokeAuth))
                onDismissDeleteAccount()
            },
            onDismiss = onDismissDeleteAccount
        )
    }

    if (showLocalPurgeDialog) {
        LocalPurgeDialog(
            title = SettingsLabels.PURGE_TITLE,
            description = SettingsLabels.PURGE_DESC,
            onDismiss = onDismissLocalPurge,
            confirmButton = {
                HoldToConfirmButton(
                    label = SettingsLabels.PURGE_CONFIRM,
                    onConfirm = {
                        presenter.onIntent(SettingsIntent.PurgeLocalData)
                        onDismissLocalPurge()
                    }
                )
            },
            dismissButton = {
                AppOutlinedButton(
                    onClick = onDismissLocalPurge,
                    modifier = Modifier.padding(end = AppTheme.dimens.medium)
                ) { AppText(text = CommonLabels.ACTION_CANCEL) }
            }
        )
    }
}