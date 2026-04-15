package fr.abknative.outgo.core.impl

import fr.abknative.outgo.core.api.EpochMillis
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.dateWithTimeIntervalSince1970

internal actual fun formatToShortDate(millis: EpochMillis): String {
    val date = NSDate.dateWithTimeIntervalSince1970(millis / 1000.0)

    val formatter = NSDateFormatter().apply {
        dateFormat = "dd MMM"
    }

    return formatter.stringFromDate(date)
}