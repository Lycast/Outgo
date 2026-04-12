package fr.abknative.outgo.android.components.shell

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.designsystem.components.buttons.AppHeaderButton
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.AccessibilityLabels
import fr.abknative.outgo.core.api.nav.AppStep

/**
 * Renders utility actions for the application header (Settings).
 * Automatically filters out the current step.
 */
@Composable
fun HeaderNavIcons(
    currentStep: AppStep,
    onNavigate: (AppStep) -> Unit
) {
    // Only Settings remains in the header utilities
    val navDestinations = listOf(AppStep.Settings)

    navDestinations.filter { it != currentStep }.forEach { step ->
        if (step == AppStep.Settings) {
            AppHeaderButton(onClick = { onNavigate(AppStep.Settings) }) {
                Icon(
                    painter = painterResource(id = R.drawable.gear_six),
                    contentDescription = AccessibilityLabels.NAVIGATE_SETTINGS,
                    tint = AppTheme.colors.primary.toColor()
                )
            }
        }
    }
}