package fr.abknative.outgo.outgoing.impl.mapper

import fr.abknative.outgo.outgoing.api.Recurrence
import fr.abknative.outgo.outgoing.api.Recurrence.UNKNOWN

/**
 * Safely parses a string into a [Recurrence].
 * Returns [Recurrence.UNKNOWN] if the value is unrecognized.
 */
internal fun mapToRecurrence(value: String): Recurrence {
    return Recurrence.entries.find {
        it.name.equals(value, ignoreCase = true)
    } ?: UNKNOWN
}