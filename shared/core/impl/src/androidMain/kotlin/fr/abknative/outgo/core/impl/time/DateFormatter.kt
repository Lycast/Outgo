package fr.abknative.outgo.core.impl.time

import fr.abknative.outgo.core.api.time.EpochMillis
import java.text.SimpleDateFormat
import java.util.*

internal actual fun formatTimestamp(millis: EpochMillis, pattern: String): String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(Date(millis))
}