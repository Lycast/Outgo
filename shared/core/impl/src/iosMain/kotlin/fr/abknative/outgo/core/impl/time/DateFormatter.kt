package fr.abknative.outgo.core.impl.time

import fr.abknative.outgo.core.api.time.EpochMillis
import platform.Foundation.*

private val formattersCache = mutableMapOf<String, NSDateFormatter>()

internal actual fun formatTimestamp(millis: EpochMillis, pattern: String): String {
    val date = NSDate.dateWithTimeIntervalSince1970(millis / 1000.0)

    val formatter = formattersCache.getOrPut(pattern) {
        NSDateFormatter().apply {
            dateFormat = pattern
            locale = NSLocale.currentLocale
        }
    }

    return formatter.stringFromDate(date)
}