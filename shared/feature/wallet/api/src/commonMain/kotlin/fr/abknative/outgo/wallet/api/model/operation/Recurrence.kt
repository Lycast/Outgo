package fr.abknative.outgo.wallet.api.model.operation

/**
 * Defines the frequency cycle of an [fr.abknative.outgo.wallet.api.model.Operation].
 */
enum class Recurrence {
    UNIQUE,
    WEEKLY,
    MONTHLY,
    YEARLY,
    UNKNOWN;

    companion object {
        /**
         * Safely parses a string into a [Recurrence].
         * Defaults to [UNKNOWN] to ensure safe deserialization from DB or Network.
         */
        fun fromString(value: String?): Recurrence {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: UNKNOWN
        }
    }
}