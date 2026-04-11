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
import fr.abknative.outgo.android.designsystem.components.buttons.AppHeaderButton
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.AccessibilityLabels
import fr.abknative.outgo.core.api.nav.AppStep

/**
 * Renders the navigation actions for the application header.
 * Automatically filters out the current step and handles premium visual cues.
 */
@Composable
fun NavigationIcons(
    currentStep: AppStep,
    isPremium: Boolean,
    onNavigate: (AppStep) -> Unit,
    onTeasingClick: () -> Unit
) {
    val navDestinations = listOf(AppStep.Analyse, AppStep.Dashboard, AppStep.Settings)

    navDestinations.filter { it != currentStep }.forEach { step ->
        when (step) {
            AppStep.Dashboard -> {
                AppHeaderButton(onClick = { onNavigate(AppStep.Dashboard) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.house_line),
                        contentDescription = AccessibilityLabels.NAVIGATE_HOME,
                        tint = AppTheme.colors.primary.toColor()
                    )
                }
            }

            AppStep.Analyse -> {
                AnalyseNavButton(
                    isPremium = isPremium,
                    onNavigate = { onNavigate(AppStep.Analyse) },
                    onTeasingClick = onTeasingClick
                )
            }

            AppStep.Settings -> {
                AppHeaderButton(onClick = { onNavigate(AppStep.Settings) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.gear_six),
                        contentDescription = AccessibilityLabels.NAVIGATE_SETTINGS,
                        tint = AppTheme.colors.primary.toColor()
                    )
                }
            }
            else -> {}
        }
    }
}

@Composable
private fun AnalyseNavButton(
    isPremium: Boolean,
    onNavigate: () -> Unit,
    onTeasingClick: () -> Unit
) {
    AppHeaderButton(
        onClick = { if (isPremium) onNavigate() else onTeasingClick() }
    ) {
        if (!isPremium) {
            BadgedBox(
                badge = { PremiumLockBadge() }
            ) {
                AnalyseIcon()
            }
        } else {
            AnalyseIcon()
        }
    }
}

@Composable
private fun AnalyseIcon() {
    Icon(
        imageVector = Icons.Default.Insights,
        contentDescription = AccessibilityLabels.NAVIGATE_ANALYSE,
        tint = AppTheme.colors.primary.toColor()
    )
}

@Composable
private fun PremiumLockBadge() {
    Box(
        modifier = Modifier
            .size(14.dp)
            .clip(CircleShape)
            .background(AppTheme.colors.surface200.toColor()),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = AccessibilityLabels.PREMIUM_BADGE,
            tint = AppTheme.colors.primary.toColor(),
            modifier = Modifier.size(8.dp)
        )
    }
}