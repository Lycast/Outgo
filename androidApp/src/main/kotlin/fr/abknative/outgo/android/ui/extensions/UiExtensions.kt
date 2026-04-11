package fr.abknative.outgo.android.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.android.ui.DashboardLabels
import fr.abknative.outgo.android.ui.FormLabels
import fr.abknative.outgo.wallet.api.model.Operation
import fr.abknative.outgo.wallet.api.model.operation.Recurrence
import kotlin.math.absoluteValue

// --- Currency Formatting ---

/**
 * Formats a Long value representing cents into a readable currency string.
 */
val Long.uiAmount: String
    get() {
        val isNegative = this < 0
        val absoluteValue = this.absoluteValue

        val euros = absoluteValue / 100
        val cents = absoluteValue % 100

        val formattedCents = cents.toString().padStart(2, '0')

        val eurosString = euros.toString()
            .reversed()
            .chunked(3)
            .joinToString(" ")
            .reversed()

        val prefix = if (isNegative) "-" else ""

        return "$prefix$eurosString,$formattedCents ${CommonLabels.CURRENCY_SYMBOL}"
    }

/**
 * Maps a [Recurrence] to its corresponding UI Color using the AppTheme.
 */
@Composable
fun Recurrence.getUiColor(): Color {
    return when (this) {
        Recurrence.YEARLY -> AppTheme.colors.textSecondary.toColor().copy(alpha = 0.5f)
        Recurrence.MONTHLY -> AppTheme.colors.primary.toColor().copy(alpha = 0.5f)
        Recurrence.WEEKLY -> AppTheme.colors.secondary.toColor().copy(alpha = 0.5f)
        Recurrence.UNIQUE -> AppTheme.colors.tertiary.toColor().copy(alpha = 0.5f)
        else -> AppTheme.colors.surface200.toColor()
    }
}

// --- Operation Entity Extensions ---

/**
 * Returns the display title for the operation, falling back to a default name if blank.
 */
val Operation.uiTitle: String
    @Composable get() = this.name.ifBlank { DashboardLabels.DEFAULT_NAME }

/**
 * Returns a summarized string representation of the operation's frequency.
 */
val Operation.uiFrequencySummary: String
    @Composable get() = when (this.recurrence) {
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
    1 -> DashboardLabels.MONTH_1 ; 2 -> DashboardLabels.MONTH_2 ; 3 -> DashboardLabels.MONTH_3
    4 -> DashboardLabels.MONTH_4 ; 5 -> DashboardLabels.MONTH_5 ; 6 -> DashboardLabels.MONTH_6
    7 -> DashboardLabels.MONTH_7 ; 8 -> DashboardLabels.MONTH_8 ; 9 -> DashboardLabels.MONTH_9
    10 -> DashboardLabels.MONTH_10; 11 -> DashboardLabels.MONTH_11; 12 -> DashboardLabels.MONTH_12
    else -> ""
}