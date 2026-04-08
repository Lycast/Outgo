package fr.abknative.outgo.android.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import fr.abknative.outgo.android.components.shell.HeaderNavIcon
import fr.abknative.outgo.android.components.shell.NavigationIcons
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor
import fr.abknative.outgo.app.nav.AppStep
import fr.abknative.outgo.sync.api.model.SyncState

/**
 * Global Header component that handles both horizontal (portrait) and vertical (landscape) layouts.
 * It dynamically displays navigation icons, hiding the icon corresponding to the current screen.
 */
@Composable
fun Header(
    modifier: Modifier = Modifier,
    syncState: SyncState,
    currentStep: AppStep,
    isPremium: Boolean,
    isVertical: Boolean = false,
    onSyncIconClick: () -> Unit,
    onNavigate: (AppStep) -> Unit,
    onTeasingClick: () -> Unit
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

    // --- Actions Logic (Defined once, used in Row or Column) ---
    val actionsContent = @Composable {
        // 1. Sync Icon (Always visible)
        HeaderNavIcon {
            SyncIconLogic(syncState = syncState, onClick = onSyncIconClick)
        }

        NavigationIcons(
            currentStep = currentStep,
            isPremium = isPremium,
            onNavigate = onNavigate,
            onTeasingClick = onTeasingClick
        )
    }

    // --- Final Layout Rendering ---
    if (isVertical) {
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
                actionsContent()
            }
        }
    } else {
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
                horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.medium)
            ) {
                actionsContent()
            }
        }
    }
}