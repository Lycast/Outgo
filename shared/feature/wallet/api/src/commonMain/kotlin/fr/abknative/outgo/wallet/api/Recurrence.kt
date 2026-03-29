package fr.abknative.outgo.wallet.api

enum class Recurrence {
    MONTHLY, YEARLY, UNKNOWN
}

/**
 * Safely parses a string into a [Recurrence].
 * Returns [Recurrence.UNKNOWN] if the value is unrecognized.
 */
fun mapToRecurrence(value: String): Recurrence {
    return Recurrence.entries.find {
        it.name.equals(value, ignoreCase = true)
    } ?: Recurrence.UNKNOWN
}