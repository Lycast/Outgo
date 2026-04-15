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

    // Ajoute un espace tous les 3 chiffres pour les milliers
    val eurosString = euros.toString().reversed().chunked(3).joinToString(" ").reversed()

    val prefix = if (isNegative) "-" else ""

    return "$prefix$eurosString,$formattedCents $currencySymbol"
}