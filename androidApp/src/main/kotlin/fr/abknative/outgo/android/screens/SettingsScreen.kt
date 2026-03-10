package fr.abknative.outgo.android.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CloudSync
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.LocalCafe
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import fr.abknative.outgo.android.components.*
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.OutgoTheme

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit,
    onCoffeeClick: () -> Unit,
    onTipsClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val scrollState = rememberScrollState()
    var showSyncModal by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Header(
                isConnected = false,
                isSettingsScreen = true,
                onSyncIconClick = { showSyncModal = true },
                onSyncNavigationClick = onNavigateBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
                .padding(AppTheme.spacing.large),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.large)
        ) {

            // --- SECTION 1 : Apparence ---
            SettingsSection(title = "Apparence") {
                SettingsRowToggle(
                    icon = Icons.Rounded.DarkMode,
                    title = "Mode Sombre",
                    subtitle = "Réduire la fatigue visuelle",
                    isChecked = isDarkMode,
                    onCheckedChange = onToggleDarkMode
                )
            }

            // --- SECTION 2 : Soutien & Communauté ---
            SettingsSection(title = "Soutenir le projet") {
                SettingsRowClickable(
                    icon = Icons.Rounded.LocalCafe,
                    title = "Offrir un café ☕",
                    subtitle = "Aidez à maintenir l'application gratuite",
                    onClick = onCoffeeClick
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                SettingsRowClickable(
                    icon = Icons.Rounded.Lightbulb,
                    title = "Astuces & Aide",
                    subtitle = "Découvrir comment optimiser son budget",
                    onClick = onTipsClick
                )
            }

            // --- SECTION 3 : Compte & Données ---
            SettingsSection(title = "Données") {
                SettingsRowClickable(
                    icon = Icons.Rounded.CloudSync,
                    title = "Synchronisation Cloud",
                    subtitle = "Sauvegardez vos données en toute sécurité",
                    onClick = { showSyncModal = true }
                )
            }

            // Version de l'app en bas
            Text(
                text = "Outgo v1.0.0",
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
                // TODO: Naviguer vers l'écran d'authentification (Semaine 5)
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
            onTipsClick = { /* Action astuces */ }
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
            onTipsClick = { }
        )
    }
}