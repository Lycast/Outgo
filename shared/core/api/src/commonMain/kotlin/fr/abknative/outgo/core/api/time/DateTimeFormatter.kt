package fr.abknative.outgo.core.api.time

/**
 * Localized date and time formatting.
 * Handles the transformation of raw timestamps into user-friendly strings.
 */
interface DateTimeFormatter {
    /** format: "12 Apr" or "12 avr." */
    fun formatShortDate(millis: EpochMillis): String

    /** format: "12 April 2026" */
    fun formatLongDate(millis: EpochMillis): String

    /** format: "April 2026" (utile pour tes en-têtes de mois) */
    fun formatMonthAndYear(millis: EpochMillis): String

    /** format: "14:30" */
    fun formatTime(millis: EpochMillis): String
}