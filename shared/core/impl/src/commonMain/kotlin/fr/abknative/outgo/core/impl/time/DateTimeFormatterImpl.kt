package fr.abknative.outgo.core.impl.time

import fr.abknative.outgo.core.api.time.DateTimeFormatter
import fr.abknative.outgo.core.api.time.EpochMillis

/**
 * Platform-agnostic delegator for string formatting.
 */
internal expect fun formatTimestamp(millis: EpochMillis, pattern: String): String

internal class DateTimeFormatterImpl : DateTimeFormatter {
    override fun formatShortDate(millis: EpochMillis): String {
        return formatTimestamp(millis, "dd MMMM")
    }

    override fun formatLongDate(millis: EpochMillis): String {
        return formatTimestamp(millis, "dd MMMM yyyy")
    }

    override fun formatMonthAndYear(millis: EpochMillis): String {
        return formatTimestamp(millis, "MMMM yyyy")
    }

    override fun formatNumericDate(millis: EpochMillis): String {
        return formatTimestamp(millis, "dd/MM/yyyy")
    }
}