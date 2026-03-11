package fr.abknative.outgo.android.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.android.ui.theme.AppTheme

@Composable
fun Header(
    modifier: Modifier = Modifier,
    isConnected: Boolean,
    isSettingsScreen: Boolean = false,
    onSyncIconClick: () -> Unit,
    onSyncNavigationClick: () -> Unit
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
                    contentDescription = if (isSettingsScreen) "Retour au Dashboard" else "Paramètres",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}