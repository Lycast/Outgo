package fr.abknative.outgo.wallet.api.model.operation

/**
 * Defines the financial direction of an [fr.abknative.outgo.wallet.api.model.Operation].
 */
enum class OperationType {
    INCOME,  // Revenu (+)
    EXPENSE;  // Dépense (-)

    companion object {
        /**
         * Safely parses a string into an [OperationType].
         * Defaults to [EXPENSE] if the value is null or unrecognized.
         */
        fun fromString(value: String?): OperationType {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: EXPENSE
        }
    }
}