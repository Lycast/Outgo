package fr.abknative.outgo.core.api.formatters

import kotlin.math.absoluteValue

/**
 * Formats a Long value representing cents into a readable currency string.
 * Handles negative values, thousands separators (spaces), and decimal formatting.
 *
 * @param currencySymbol The symbol to append at the end (e.g., "€", "$").
 * @return A formatted string, e.g., "1 234,50 €"
 */
fun Long.formatAsCurrency(currencySymbol: String): String {
    val isNegative = this < 0
    val absoluteValue = this.absoluteValue

    val euros = absoluteValue / 100
    val cents = absoluteValue % 100

    val formattedCents = cents.toString().padStart(2, '0')

    val eurosString = euros.toString().reversed().chunked(3).joinToString(" ").reversed()

    val prefix = if (isNegative) "-" else ""

    return "$prefix$eurosString,$formattedCents $currencySymbol"
}

/**
 * Formats a Long value representing cents into a raw string suitable for text inputs.
 * e.g., 1450 -> "14.5" or "14.50"
 */
fun Long.formatForInput(): String {
    val decimalAmount = this / 100.0
    return if (decimalAmount % 1 == 0.0) {
        decimalAmount.toInt().toString()
    } else {
        decimalAmount.toString()
    }
}