package fr.abknative.outgo.android.core.extensions

import androidx.compose.runtime.Composable
import fr.abknative.outgo.android.core.CommonLabels
import fr.abknative.outgo.android.core.ListLabels
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
    1 -> ListLabels.MONTH_1; 2 -> ListLabels.MONTH_2; 3 -> ListLabels.MONTH_3
    4 -> ListLabels.MONTH_4; 5 -> ListLabels.MONTH_5; 6 -> ListLabels.MONTH_6
    7 -> ListLabels.MONTH_7; 8 -> ListLabels.MONTH_8; 9 -> ListLabels.MONTH_9
    10 -> ListLabels.MONTH_10; 11 -> ListLabels.MONTH_11; 12 -> ListLabels.MONTH_12
    else -> ""
}