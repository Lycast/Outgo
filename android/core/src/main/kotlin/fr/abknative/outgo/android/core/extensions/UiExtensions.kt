package fr.abknative.outgo.android.core.extensions

import androidx.compose.runtime.Composable
import fr.abknative.outgo.android.core.CommonLabels
import fr.abknative.outgo.core.api.formatters.formatAsCurrency

// --- Currency Formatting ---

/**
 * Helper Compose extension to quickly format a Long (cents) into a UI-ready string
 * using the shared KMP formatter and the Compose string resources.
 */
val Long.uiAmount: String
    @Composable get() = this.formatAsCurrency(CommonLabels.CURRENCY_SYMBOL)

// --- Date Formatting ---

/**
 * Maps an integer to its corresponding localized month name.
 */
@Composable
fun getMonthName(month: Int): String = when (month) {
    1 -> CommonLabels.MONTH_1; 2 -> CommonLabels.MONTH_2; 3 -> CommonLabels.MONTH_3
    4 -> CommonLabels.MONTH_4; 5 -> CommonLabels.MONTH_5; 6 -> CommonLabels.MONTH_6
    7 -> CommonLabels.MONTH_7; 8 -> CommonLabels.MONTH_8; 9 -> CommonLabels.MONTH_9
    10 -> CommonLabels.MONTH_10; 11 -> CommonLabels.MONTH_11; 12 -> CommonLabels.MONTH_12
    else -> ""
}