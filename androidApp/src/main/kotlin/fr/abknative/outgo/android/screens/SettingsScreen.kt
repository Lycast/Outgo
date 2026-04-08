package fr.abknative.outgo.android.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.components.common.ConfirmationDialog
import fr.abknative.outgo.android.components.common.HoldToConfirmButton
import fr.abknative.outgo.android.components.common.PrimaryButton
import fr.abknative.outgo.android.components.common.SecondaryButton
import fr.abknative.outgo.android.components.settings.DeleteAccountDialog
import fr.abknative.outgo.android.components.settings.SettingsRowClickable
import fr.abknative.outgo.android.components.settings.SettingsRowToggle
import fr.abknative.outgo.android.components.settings.SettingsSection
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.android.ui.DialogLabels
import fr.abknative.outgo.android.ui.SettingsLabels
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor
import fr.abknative.outgo.settings.api.SettingsIntent
import fr.abknative.outgo.settings.api.SettingsPresenter

@Composable
fun SettingsScreen(
    presenter: SettingsPresenter,
    onNavigateToLogin: () -> Unit,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by presenter.state.collectAsState()
    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current

    // --- Navigation Effect ---
    LaunchedEffect(state.actionSuccess) {
        if (state.actionSuccess) {
            presenter.onIntent(SettingsIntent.ResetSuccessFlag)
        }
    }

    // --- Dialog States ---
    var showLogoutConfirm by remember { mutableStateOf(false) }
    var showDeleteAccountConfirm by remember { mutableStateOf(false) }
    var showPurgeConfirm by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(AppTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.large)
    ) {

        // --- SECTION Apparence ---
        SettingsSection(title = SettingsLabels.SECTION_APPEARANCE) {
            SettingsRowToggle(
                icon = R.drawable.moon_duotone,
                title = SettingsLabels.DARK_MODE_TITLE,
                subtitle = SettingsLabels.DARK_MODE_SUBTITLE,
                isChecked = isDarkMode,
                onCheckedChange = onToggleDarkMode
            )
        }

        // --- SECTION Soutien & Communauté ---
        SettingsSection(title = SettingsLabels.SECTION_SUPPORT) {
            SettingsRowClickable(
                icon = R.drawable.lightbulb_duotone,
                title = SettingsLabels.TIPS_TITLE,
                subtitle = SettingsLabels.TIPS_SUBTITLE,
                onClick = { uriHandler.openUri(SettingsLabels.URL_SITE) }
            )
            HorizontalDivider(color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f))
            SettingsRowClickable(
                icon = R.drawable.envelope_duotone,
                title = SettingsLabels.CONTACT_TITLE,
                subtitle = SettingsLabels.CONTACT_SUBTITLE,
                onClick = { uriHandler.openUri(SettingsLabels.URL_CONTACT) }
            )
        }

        // --- SECTION Données & Compte ---
        if (state.session == null) {
            SettingsSection(title = SettingsLabels.SECTION_DATA_AND_ACCOUNT) {
                SettingsRowClickable(
                    icon = R.drawable.arrows_clockwise_duotone,
                    title = SettingsLabels.SYNC_TITLE,
                    subtitle = SettingsLabels.SYNC_SUBTITLE,
                    onClick = { onNavigateToLogin() }
                )
                HorizontalDivider(color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f))
                SettingsRowClickable(
                    icon = R.drawable.trash_duotone,
                    title = SettingsLabels.PURGE_TITLE,
                    subtitle = SettingsLabels.PURGE_SUBTITLE,
                    onClick = { showPurgeConfirm = true }
                )
            }
        } else {
            SettingsSection(title = SettingsLabels.SECTION_ACCOUNT) {
                SettingsRowClickable(
                    icon = R.drawable.sign_out_duotone,
                    title = SettingsLabels.LOGOUT_TITLE,
                    subtitle = SettingsLabels.LOGOUT_SUBTITLE,
                    onClick = { showLogoutConfirm = true }
                )
                HorizontalDivider(color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f))
                SettingsRowClickable(
                    icon = R.drawable.trash_duotone,
                    title = SettingsLabels.DELETE_ACCOUNT_TITLE,
                    subtitle = SettingsLabels.DELETE_ACCOUNT_SUBTITLE,
                    onClick = { showDeleteAccountConfirm = true }
                )
            }
        }

        // Footer
        Text(
            text = SettingsLabels.APP_VERSION_PREFIX,
            style = AppTheme.typo.caption,
            color = AppTheme.colors.textPrimary.toColor(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = AppTheme.spacing.large),
            textAlign = TextAlign.Center
        )
    }

    // --- MODAL DE CONFIRMATION ---
    if (showLogoutConfirm) {
        ConfirmationDialog(
            title = DialogLabels.LOGOUT_TITLE,
            description = DialogLabels.LOGOUT_DESC,
            onDismiss = { showLogoutConfirm = false },
            confirmButton = {
                PrimaryButton(
                    label = DialogLabels.LOGOUT_CONFIRM,
                    labelColor = AppTheme.colors.primary.toColor(),
                    containerColor = AppTheme.colors.primary.toColor().copy(alpha = 0.1f),
                    onClick = {
                        presenter.onIntent(SettingsIntent.Logout)
                        showLogoutConfirm = false
                    }
                )
            },
            dismissButton = {
                SecondaryButton(
                    label = CommonLabels.ACTION_CANCEL,
                    labelColor = AppTheme.colors.textSecondary.toColor(),
                    onClick = { showLogoutConfirm = false },
                    modifier = Modifier.padding(end = AppTheme.spacing.medium)
                )
            }
        )
    }

    if (showDeleteAccountConfirm) {
        DeleteAccountDialog(
            onConfirm = { wipeLocal, wipeServer, revokeAuth ->
                presenter.onIntent(SettingsIntent.DeleteAccount(wipeLocal, wipeServer, revokeAuth))
                showDeleteAccountConfirm = false
            },
            onDismiss = { showDeleteAccountConfirm = false }
        )
    }

    if (showPurgeConfirm) {
        ConfirmationDialog(
            title = DialogLabels.PURGE_TITLE,
            description = DialogLabels.PURGE_DESC,
            onDismiss = { showPurgeConfirm = false },
            confirmButton = {
                HoldToConfirmButton(
                    label = DialogLabels.PURGE_CONFIRM,
                    onConfirm = {
                        presenter.onIntent(SettingsIntent.PurgeLocalData)
                        showPurgeConfirm = false
                    }
                )
            },
            dismissButton = {
                SecondaryButton(
                    label = CommonLabels.ACTION_CANCEL,
                    labelColor = AppTheme.colors.textSecondary.toColor(),
                    onClick = { showPurgeConfirm = false },
                    modifier = Modifier.padding(end = AppTheme.spacing.medium)
                )
            }
        )
    }
}
