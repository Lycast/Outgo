package fr.abknative.outgo.android.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.components.common.ConfirmationDialog
import fr.abknative.outgo.android.components.common.Header
import fr.abknative.outgo.android.components.common.SyncPromotionModal
import fr.abknative.outgo.android.components.settings.SettingsRowClickable
import fr.abknative.outgo.android.components.settings.SettingsRowToggle
import fr.abknative.outgo.android.components.settings.SettingsSection
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.android.ui.DialogLabels
import fr.abknative.outgo.android.ui.SettingsLabels
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.OutgoTheme
import fr.abknative.outgo.android.ui.theme.toColor
import fr.abknative.outgo.auth.api.model.UserSession
import fr.abknative.outgo.dashboard.api.SyncUiState

@Composable
fun SettingsScreen(
    session: UserSession?,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit,
    onPurgeLocalData: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onTipsClick: () -> Unit,
    onContactClick: () -> Unit,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    val scrollState = rememberScrollState()

    // --- États pour les Dialogues ---
    var showLogoutConfirm by remember { mutableStateOf(false) }
    var showDeleteAccountConfirm by remember { mutableStateOf(false) }
    var showPurgeConfirm by remember { mutableStateOf(false) }
    var showSyncModal by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        topBar = {
            if (!isLandscape) {
                Header(
                    syncState = if (session == null) SyncUiState.UNAUTHENTICATED else SyncUiState.UP_TO_DATE,
                    isVertical = false,
                    isSettingsScreen = true,
                    onSyncIconClick = { if (session == null) showSyncModal = true },
                    onSyncNavigationClick = onNavigateBack,
                )
            }
        }
    ) { paddingValues ->

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (isLandscape) PaddingValues(0.dp) else paddingValues)
        ) {
            if (isLandscape) {
                Header(
                    syncState = if (session == null) SyncUiState.UNAUTHENTICATED else SyncUiState.UP_TO_DATE,
                    isVertical = true,
                    isSettingsScreen = true,
                    onSyncIconClick = { if (session == null) showSyncModal = true },
                    onSyncNavigationClick = onNavigateBack,
                )
                VerticalDivider(
                    thickness = 1.dp,
                    color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(AppTheme.spacing.medium),
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.large)
            ) {

                // --- SECTION 1 : Apparence ---
                SettingsSection(title = SettingsLabels.SECTION_APPEARANCE) {
                    SettingsRowToggle(
                        icon = R.drawable.moon_duotone,
                        title = SettingsLabels.DARK_MODE_TITLE,
                        subtitle = SettingsLabels.DARK_MODE_SUBTITLE,
                        isChecked = isDarkMode,
                        onCheckedChange = onToggleDarkMode
                    )
                }

                // --- SECTION 2 : Soutien & Communauté ---
                SettingsSection(title = SettingsLabels.SECTION_SUPPORT) {
                    SettingsRowClickable(
                        icon = R.drawable.lightbulb_duotone,
                        title = SettingsLabels.TIPS_TITLE,
                        subtitle = SettingsLabels.TIPS_SUBTITLE,
                        onClick = onTipsClick
                    )
                    HorizontalDivider(color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f))
                    SettingsRowClickable(
                        icon = R.drawable.envelope_duotone,
                        title = SettingsLabels.CONTACT_TITLE,
                        subtitle = SettingsLabels.CONTACT_SUBTITLE,
                        onClick = onContactClick
                    )
                }


                // --- SECTION 3 : Données Locales ---
                SettingsSection(title = SettingsLabels.SECTION_DATA) {
                    SettingsRowClickable(
                        icon = R.drawable.trash_duotone,
                        title = "Vider le cache local", // TODO: Labels
                        subtitle = "Supprime les données de l'appareil sans toucher au serveur.",
                        onClick = { showPurgeConfirm = true }
                    )
                }

                // --- SECTION 4 : Compte ---
                if (session == null) {
                    SettingsSection(title = SettingsLabels.SECTION_ACCOUNT) {
                        SettingsRowClickable(
                            icon = R.drawable.arrows_clockwise_duotone,
                            title = SettingsLabels.SYNC_TITLE,
                            subtitle = SettingsLabels.SYNC_SUBTITLE,
                            onClick = { showSyncModal = true }
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


                // Version de l'app (Footer)
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
        }
    }

    if (showLogoutConfirm) {
        ConfirmationDialog(
            title = DialogLabels.LOGOUT_TITLE,
            description = DialogLabels.LOGOUT_DESC,
            confirmLabel = DialogLabels.LOGOUT_CONFIRM,
            cancelLabel = CommonLabels.ACTION_CANCEL,
            isDestructive = true,
            onConfirm = {
                onLogout()
                showLogoutConfirm = false
            },
            onDismiss = { showLogoutConfirm = false }
        )
    }

    if (showDeleteAccountConfirm) {
        ConfirmationDialog(
            title = DialogLabels.DELETE_ACCOUNT_TITLE,
            description = DialogLabels.DELETE_ACCOUNT_DESC,
            confirmLabel = DialogLabels.DELETE_ACCOUNT_CONFIRM,
            cancelLabel = CommonLabels.ACTION_CANCEL,
            isDestructive = true,
            onConfirm = {
                onDeleteAccount()
                showDeleteAccountConfirm = false
            },
            onDismiss = { showDeleteAccountConfirm = false }
        )
    }

    if (showPurgeConfirm) {
        ConfirmationDialog(
            title = DialogLabels.PURGE_TITLE,
            description = DialogLabels.PURGE_DESC,
            confirmLabel = DialogLabels.PURGE_CONFIRM,
            cancelLabel = CommonLabels.ACTION_CANCEL,
            isDestructive = true,
            onConfirm = {
                onPurgeLocalData()
                showPurgeConfirm = false
            },
            onDismiss = { showPurgeConfirm = false }
        )
    }

    if (showSyncModal) {
        SyncPromotionModal(
            onDismiss = { showSyncModal = false },
            onNavigateToLogin = {
                showSyncModal = false
                onNavigateToLogin()
            }
        )
    }
}

@Preview(showBackground = true, name = "Settings - Mode Clair")
@Composable
fun PreviewSettingsScreen() {

    OutgoTheme(darkTheme = false) {
        SettingsScreen(
            session = null,
            onLogout = { },
            onDeleteAccount = {},
            onPurgeLocalData = {},
            onNavigateBack = { /* Navigation retour */ },
            onNavigateToLogin = { },
            onTipsClick = { /* Action astuces */ },
            onContactClick = { /* Action contact */ },
            isDarkMode = false,
            onToggleDarkMode = { },
        )
    }
}

@Preview(showBackground = true, name = "Settings - Mode Sombre", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSettingsScreenDark() {
    OutgoTheme(darkTheme = true) {
        SettingsScreen(
            session = null,
            onLogout = { },
            onDeleteAccount = {},
            onPurgeLocalData = {},
            onNavigateBack = { /* Navigation retour */ },
            onNavigateToLogin = { },
            onTipsClick = { /* Action astuces */ },
            onContactClick = { /* Action contact */ },
            isDarkMode = true,
            onToggleDarkMode = {},
        )
    }
}