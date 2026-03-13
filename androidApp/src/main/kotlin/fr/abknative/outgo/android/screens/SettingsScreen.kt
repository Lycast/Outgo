package fr.abknative.outgo.android.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import fr.abknative.outgo.android.components.common.Header
import fr.abknative.outgo.android.components.common.SyncPromotionModal
import fr.abknative.outgo.android.components.settings.SettingsRowClickable
import fr.abknative.outgo.android.components.settings.SettingsRowToggle
import fr.abknative.outgo.android.components.settings.SettingsSection
import fr.abknative.outgo.android.ui.SettingsLabels
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.OutgoTheme

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    onCoffeeClick: () -> Unit,
    onTipsClick: () -> Unit,
    onContactClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val scrollState = rememberScrollState()
    var showSyncModal by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Header(
                isConnected = false, // todo À lier à ton AuthState plus tard
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
                    icon = Icons.Rounded.DarkMode,
                    title = SettingsLabels.DARK_MODE_TITLE,
                    subtitle = SettingsLabels.DARK_MODE_SUBTITLE,
                    isChecked = isDarkMode,
                    onCheckedChange = onToggleDarkMode
                )
            }

            // --- SECTION 2 : Soutien & Communauté ---
            SettingsSection(title = SettingsLabels.SECTION_SUPPORT) {
                SettingsRowClickable(
                    icon = Icons.Rounded.Lightbulb,
                    title = SettingsLabels.TIPS_TITLE,
                    subtitle = SettingsLabels.TIPS_SUBTITLE,
                    onClick = onTipsClick
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f))
                SettingsRowClickable(
                    icon = Icons.Rounded.Email,
                    title = SettingsLabels.CONTACT_TITLE,
                    subtitle = SettingsLabels.CONTACT_SUBTITLE,
                    onClick = onContactClick
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f))
                SettingsRowClickable(
                    icon = Icons.Rounded.LocalCafe,
                    title = SettingsLabels.COFFEE_TITLE,
                    subtitle = SettingsLabels.COFFEE_SUBTITLE,
                    onClick = { /*onCoffeeClick*/ }
                )
            }

            // --- SECTION 3 : Compte & Données ---
            SettingsSection(title = SettingsLabels.SECTION_DATA) {
                SettingsRowClickable(
                    icon = Icons.Rounded.CloudSync,
                    title = SettingsLabels.SYNC_TITLE,
                    subtitle = SettingsLabels.SYNC_SUBTITLE,
                    onClick = { showSyncModal = true }
                )
            }

            // Version de l'app (Footer)
            Text(
                text = SettingsLabels.APP_VERSION_PREFIX,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.spacing.large),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
    if (showSyncModal) {
        SyncPromotionModal(
            onDismiss = { showSyncModal = false },
            onNavigateToLogin = {
                showSyncModal = false
                // TODO: Navigation vers l'Auth (Semaine 5)
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
            onNavigateBack = { /* Navigation retour */ },
            isDarkMode = darkMode,
            onToggleDarkMode = { darkMode = it },
            onCoffeeClick = { /* Action café */ },
            onTipsClick = { /* Action astuces */ },
            onContactClick = { /* Action contact */ }
        )
    }
}

@Preview(showBackground = true, name = "Settings - Mode Sombre", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSettingsScreenDark() {
    OutgoTheme(darkTheme = true) {
        SettingsScreen(
            onNavigateBack = { },
            isDarkMode = true,
            onToggleDarkMode = { },
            onCoffeeClick = { },
            onTipsClick = { },
            onContactClick = { }
        )
    }
}