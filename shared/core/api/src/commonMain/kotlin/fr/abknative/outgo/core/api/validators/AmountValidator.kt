package fr.abknative.outgo.core.api.validators

/**
 * Utility object responsible for validating currency amount inputs.
 * Ensures the input contains only digits and a maximum of two decimal places.
 */
object AmountValidator {
    private const val MAX_DIGITS_BEFORE_COMMA = 7
    private val amountRegex = Regex("""^\d{0,$MAX_DIGITS_BEFORE_COMMA}(\.?\d{0,2})$""")

    /**
     * Validates and sanitizes the user input for an amount.
     * Replaces commas with dots and checks against the defined regex pattern.
     *
     * @param input The raw string input from the user.
     * @return The sanitized string if valid, or null if the input breaks the formatting rules.
     */
    fun validate(input: String): String? {
        val sanitized = input.replace(',', '.')
        return if (sanitized.isEmpty() || sanitized.matches(amountRegex)) {
            sanitized
        } else {
            null
        }
    }
}