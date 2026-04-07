package fr.abknative.outgo.android.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.ui.AccessibilityLabels
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.OutgoTheme
import fr.abknative.outgo.android.ui.theme.toColor
import fr.abknative.outgo.core.api.model.SyncUiState

@Composable
fun Header(
    modifier: Modifier = Modifier,
    syncState: SyncUiState,
    isVertical: Boolean = false, // 👈 Nouveau paramètre
    isSettingsScreen: Boolean = false,
    onSyncIconClick: () -> Unit,
    onSyncNavigationClick: () -> Unit,
) {
    val containerModifier = if (isVertical) {
        modifier
            .fillMaxHeight()
            .width(intrinsicSize = IntrinsicSize.Min)
            .padding(vertical = AppTheme.spacing.large)
    } else {
        modifier
            .fillMaxWidth()
            .padding(top = AppTheme.spacing.big)
            .padding(bottom = AppTheme.spacing.medium)
            .padding(horizontal = AppTheme.spacing.large)
    }

    val actionsLayout = @Composable {
        GlassCard(modifier = Modifier.size(42.dp)) {
            SyncIconLogic(
                syncState = syncState,
                onClick = onSyncIconClick
            )
        }

        Spacer(modifier = Modifier.width(AppTheme.spacing.extraSmall))

        GlassCard(modifier = Modifier.size(42.dp)) {
            IconButton(onClick = onSyncNavigationClick) {
                Icon(
                    painter = painterResource(id = if (isSettingsScreen) R.drawable.house_line else R.drawable.gear_six),
                    contentDescription = if (isSettingsScreen) AccessibilityLabels.NAVIGATE_HOME else AccessibilityLabels.NAVIGATE_SETTINGS,
                    tint = AppTheme.colors.primary.toColor()
                )
            }
        }
    }

    if (isVertical) {
        // --- Disposition Paysage (Rail gauche) ---
        Column(
            modifier = containerModifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = CommonLabels.APP_NAME,
                style = AppTheme.typo.title,
                fontSize = 24.sp,
                color = AppTheme.colors.primary.toColor(),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = AppTheme.spacing.medium).padding(top = AppTheme.spacing.medium)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.medium)
            ) {
                actionsLayout()
            }
        }
    } else {
        // --- Disposition Portrait (Top Bar classique) ---
        Row(
            modifier = containerModifier,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = CommonLabels.APP_NAME,
                style = AppTheme.typo.title,
                fontSize = 24.sp,
                color = AppTheme.colors.primary.toColor()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.small)
            ) {
                actionsLayout()
            }
        }
    }
}

@Preview(showBackground = true, name = "Light Mode - Horizontal")
@Composable
fun HeaderPreviewHorizontal() {
    OutgoTheme {
        Surface(color = AppTheme.colors.background.toColor()) {
            Header(
                syncState = SyncUiState.IN_PROGRESS,
                isVertical = false,
                isSettingsScreen = false,
                onSyncIconClick = {},
                onSyncNavigationClick = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "Light Mode - Vertical", widthDp = 100, heightDp = 400)
@Composable
fun HeaderPreviewVertical() {
    OutgoTheme {
        Surface(color = AppTheme.colors.background.toColor()) {
            Header(
                syncState = SyncUiState.IN_PROGRESS,
                isVertical = true,
                isSettingsScreen = false,
                onSyncIconClick = {},
                onSyncNavigationClick = {},
            )
        }
    }
}