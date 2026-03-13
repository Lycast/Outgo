package fr.abknative.outgo.android.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import fr.abknative.outgo.android.ui.AccessibilityLabels
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.OutgoTheme

@Composable
fun Header(
    modifier: Modifier = Modifier,
    isConnected: Boolean,
    isSettingsScreen: Boolean = false,
    onSyncIconClick: () -> Unit,
    onSyncNavigationClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = AppTheme.spacing.extraLarge)
            .padding(horizontal = AppTheme.spacing.large),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- 1. Titre (Gauche) ---
        Text(
            text = CommonLabels.APP_NAME,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.primary
        )

        // --- 2. Actions (Droite) ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.small)
        ) {

            // Icône Cloud
            SyncIconLogic(
                isConnected = isConnected,
                onClick = onSyncIconClick
            )

            // Icône Navigation (Settings <-> Home)
            IconButton(
                onClick = onSyncNavigationClick
            ) {
                Icon(
                    imageVector = if (isSettingsScreen) Icons.Rounded.Home else Icons.Rounded.Settings,
                    contentDescription = if (isSettingsScreen) AccessibilityLabels.NAVIGATE_HOME else AccessibilityLabels.NAVIGATE_SETTINGS,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, name = "Dark Mode", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HeaderPreview() {
    OutgoTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Header(
                isConnected = true,
                isSettingsScreen = false,
                onSyncIconClick = {},
                onSyncNavigationClick = {},
            )
        }
    }
}