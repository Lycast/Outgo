package fr.abknative.outgo.android.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.components.common.Header
import fr.abknative.outgo.android.components.common.SyncPromotionModal
import fr.abknative.outgo.android.components.settings.SettingsRowClickable
import fr.abknative.outgo.android.components.settings.SettingsRowToggle
import fr.abknative.outgo.android.components.settings.SettingsSection
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
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onCoffeeClick: () -> Unit,
    onTipsClick: () -> Unit,
    onContactClick: () -> Unit,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    val scrollState = rememberScrollState()
    var showSyncModal by remember { mutableStateOf(false) }
    var showDeleteAccountConfirmation by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        topBar = {
            Header(
                syncState = SyncUiState.UNAUTHENTICATED, // todo À lier à ton AuthState plus tard
                isSettingsScreen = true,
                onSyncIconClick = { showSyncModal = true },
                onSyncNavigationClick = onNavigateBack,
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
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
                HorizontalDivider(color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f))
                SettingsRowClickable(
                    icon = R.drawable.coffee_duotone,
                    title = SettingsLabels.COFFEE_TITLE,
                    subtitle = SettingsLabels.COFFEE_SUBTITLE,
                    onClick = { /*onCoffeeClick*/ }
                )
            }


            // --- SECTION 3 : Compte & Données ---
            if (session == null) {
                SettingsSection(title = SettingsLabels.SECTION_DATA) {
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
                        onClick = onLogout
                    )
                    HorizontalDivider(color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f))
                    SettingsRowClickable(
                        icon = R.drawable.trash_duotone,
                        title = SettingsLabels.DELETE_ACCOUNT_TITLE,
                        subtitle = SettingsLabels.DELETE_ACCOUNT_SUBTITLE,
                        onClick = { showDeleteAccountConfirmation = true }
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
    var darkMode by remember { mutableStateOf(false) }

    OutgoTheme(darkTheme = darkMode) {
        SettingsScreen(
            session = null,
            onNavigateBack = { /* Navigation retour */ },
            onNavigateToLogin = { },
            isDarkMode = darkMode,
            onToggleDarkMode = { darkMode = it },
            onCoffeeClick = { /* Action café */ },
            onTipsClick = { /* Action astuces */ },
            onContactClick = { /* Action contact */ },
            onLogout = { },
            onDeleteAccount = { }
        )
    }
}

@Preview(showBackground = true, name = "Settings - Mode Sombre", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSettingsScreenDark() {
    OutgoTheme(darkTheme = true) {
        SettingsScreen(
            session = null,
            onNavigateBack = { },
            onNavigateToLogin = { },
            isDarkMode = true,
            onToggleDarkMode = { },
            onCoffeeClick = { },
            onTipsClick = { },
            onContactClick = { },
            onLogout = { },
            onDeleteAccount = { }
        )
    }
}