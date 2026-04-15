package fr.abknative.outgo.core.api.validators

/**
 * Utility object responsible for validating operation or wallet names.
 */
object NameValidator {
    private const val MAX_LENGTH = 30

    /**
     * Validates and sanitizes the length of the provided name.
     *
     * @param input The raw string input from the user.
     * @return The input string truncated to the maximum allowed length.
     */
    fun validate(input: String): String {
        return input.take(MAX_LENGTH)
    }
}