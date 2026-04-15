package fr.abknative.outgo.android.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.android.ui.FormLabels
import fr.abknative.outgo.android.ui.ListLabels
import fr.abknative.outgo.core.api.formatters.formatAsCurrency
import fr.abknative.outgo.wallet.api.model.Operation
import fr.abknative.outgo.wallet.api.model.operation.Recurrence

// --- Currency Formatting ---

/**
 * Helper Compose extension to quickly format a Long (cents) into a UI-ready string
 * using the shared KMP formatter and the Compose string resources.
 */
val Long.uiAmount: String
    @Composable get() = this.formatAsCurrency(CommonLabels.CURRENCY_SYMBOL)

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

// --- Operation Entity Extensions ---

/**
 * Returns the display title for the operation, falling back to a default name if blank.
 */
val Operation.uiTitle: String
    @Composable get() = this.name.ifBlank { ListLabels.DEFAULT_NAME }

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

/**
 * Maps an integer to its corresponding localized month name.
 */
@Composable
fun getMonthName(month: Int): String = when (month) {
    1 -> ListLabels.MONTH_1; 2 -> ListLabels.MONTH_2; 3 -> ListLabels.MONTH_3
    4 -> ListLabels.MONTH_4; 5 -> ListLabels.MONTH_5; 6 -> ListLabels.MONTH_6
    7 -> ListLabels.MONTH_7; 8 -> ListLabels.MONTH_8; 9 -> ListLabels.MONTH_9
    10 -> ListLabels.MONTH_10; 11 -> ListLabels.MONTH_11; 12 -> ListLabels.MONTH_12
    else -> ""
}