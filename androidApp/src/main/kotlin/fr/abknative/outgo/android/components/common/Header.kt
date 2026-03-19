package fr.abknative.outgo.android.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.ui.AccessibilityLabels
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.OutgoTheme
import fr.abknative.outgo.android.ui.theme.toColor

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
            .padding(top = AppTheme.spacing.big)
            .padding(horizontal = AppTheme.spacing.large),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- Titre (Gauche) ---
        Text(
            text = CommonLabels.APP_NAME,
            style = AppTheme.typo.title,
            color = AppTheme.colors.primary.toColor()
        )

        // --- Actions (Droite) ---
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
                    painter = painterResource( id = if (isSettingsScreen) R.drawable.house_line  else R.drawable.gear_six),
                    contentDescription = if (isSettingsScreen) AccessibilityLabels.NAVIGATE_HOME else AccessibilityLabels.NAVIGATE_SETTINGS,
                    tint = AppTheme.colors.primary.toColor()
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
        Surface(color = AppTheme.colors.background.toColor()) {
            Header(
                isConnected = true,
                isSettingsScreen = false,
                onSyncIconClick = {},
                onSyncNavigationClick = {},
            )
        }
    }
}