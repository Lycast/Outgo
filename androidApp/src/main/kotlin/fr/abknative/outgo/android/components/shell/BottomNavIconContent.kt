package fr.abknative.outgo.android.components.shell

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.AccessibilityLabels
import fr.abknative.outgo.core.api.nav.AppStep

/**
 * Renders the icon content for a navigation step, handling selection colors and badges.
 *
 * @param step The navigation step to represent.
 * @param isSelected Whether this step is the currently active one.
 * @param isLocked Whether the step should display a premium lock badge.
 */
@Composable
fun BottomNavIconContent(
    step: AppStep,
    isSelected: Boolean,
    isLocked: Boolean
) {
    val tint = if (isSelected) AppTheme.colors.textOnBrand.toColor() else AppTheme.colors.textSecondary.toColor()

    val icon = @Composable {
        when (step) {
            AppStep.Dashboard -> Icon(
                painter = painterResource(id = R.drawable.house_line),
                contentDescription = AccessibilityLabels.NAVIGATE_HOME,
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
            AppStep.Analyse -> Icon(
                imageVector = Icons.Default.Insights,
                contentDescription = AccessibilityLabels.NAVIGATE_ANALYSE,
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
            else -> {}
        }
    }

    if (isLocked) {
        BadgedBox(
            badge = {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(AppTheme.colors.tertiary.toColor()),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = AppTheme.colors.textOnBrand.toColor(),
                        modifier = Modifier.size(10.dp)
                    )
                }
            }
        ) {
            icon()
        }
    } else {
        icon()
    }
}