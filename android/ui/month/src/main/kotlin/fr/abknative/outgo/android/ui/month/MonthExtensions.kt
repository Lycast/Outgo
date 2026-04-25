package fr.abknative.outgo.android.ui.month

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import fr.abknative.outgo.android.core.FormLabels
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor
import fr.abknative.outgo.wallet.api.model.operation.Recurrence

/**
 * Maps a [Recurrence] to its corresponding UI Color using the AppTheme.
 */
@Composable
fun Recurrence.getUiColor(): Color {
    return when (this) {
        Recurrence.YEARLY -> AppTheme.colors.textSecondary.toColor()
        Recurrence.MONTHLY -> AppTheme.colors.primary.toColor()
        Recurrence.WEEKLY -> AppTheme.colors.secondary.toColor()
        Recurrence.UNIQUE -> AppTheme.colors.tertiary.toColor()
        else -> AppTheme.colors.surface200.toColor()
    }
}

/**
 * Returns the localized label for a [Recurrence] type.
 */
val Recurrence.uiLabel: String
    @Composable get() = when (this) {
        Recurrence.UNIQUE -> FormLabels.CYCLE_UNIQUE
        Recurrence.WEEKLY -> FormLabels.CYCLE_WEEKLY
        Recurrence.MONTHLY -> FormLabels.CYCLE_MONTHLY
        Recurrence.YEARLY -> FormLabels.CYCLE_YEARLY
        Recurrence.UNKNOWN -> ""
    }