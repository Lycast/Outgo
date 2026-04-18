package fr.abknative.outgo.core.impl

import fr.abknative.outgo.core.api.time.EpochMillis
import java.text.SimpleDateFormat
import java.util.*

internal actual fun formatToShortDate(millis: EpochMillis): String {
    val formatter = SimpleDateFormat("dd MMM", Locale.getDefault())
    return formatter.format(Date(millis))
}