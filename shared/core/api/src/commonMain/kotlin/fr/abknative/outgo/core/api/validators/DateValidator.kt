package fr.abknative.outgo.core.api.validators

import fr.abknative.outgo.core.api.EpochMillis
import fr.abknative.outgo.core.api.TimeProvider

/**
 * Helper class to validate partial and complete date inputs formatted as "ddMMyyyy".
 *
 * @property timeProvider The provider used to calculate month bounds and leap years.
 */
class DateValidator(private val timeProvider: TimeProvider) {

    /**
     * Checks if the currently typed string is a potentially valid date format.
     * Validates partial inputs (e.g., prevents day starting with 4).
     *
     * @param textBuffer The current partial or complete date string.
     * @return True if the partial input is allowed, false if it contains impossible date combinations.
     */
    fun isPartialInputValid(textBuffer: String): Boolean {
        if (textBuffer.isEmpty()) return true
        if (!textBuffer.all { it.isDigit() }) return false
        if (textBuffer.length > 8) return false

        if (textBuffer.isNotEmpty()) {
            val firstDigit = textBuffer[0].digitToIntOrNull() ?: return false
            if (firstDigit > 3) return false
        }
        if (textBuffer.length >= 2) {
            val day = textBuffer.substring(0, 2).toIntOrNull() ?: return false
            if (day !in 1..31) return false
        }
        if (textBuffer.length >= 3) {
            val thirdDigit = textBuffer[2].digitToIntOrNull() ?: return false
            if (thirdDigit > 1) return false
        }
        if (textBuffer.length >= 4) {
            val month = textBuffer.substring(2, 4).toIntOrNull() ?: return false
            if (month !in 1..12) return false
        }
        if (textBuffer.length >= 5) {
            val fifthDigit = textBuffer[4].digitToIntOrNull() ?: return false
            if (fifthDigit != 2) return false
        }
        if (textBuffer.length == 8) {
            return isCompleteDateValid(textBuffer)
        }
        return true
    }

    /**
     * Verifies if a complete 8-character string corresponds to a real calendar date.
     *
     * @param dateStr The complete date string in "ddMMyyyy" format.
     * @return True if the date exists, false otherwise.
     */
    fun isCompleteDateValid(dateStr: String): Boolean {
        if (dateStr.length != 8) return false
        val day = dateStr.substring(0, 2).toIntOrNull() ?: return false
        val month = dateStr.substring(2, 4).toIntOrNull() ?: return false
        val year = dateStr.substring(4, 8).toIntOrNull() ?: return false

        if (month !in 1..12) return false
        if (year !in 2000..2100) return false

        val startOfMonth = timeProvider.startOfMonth(month, year)
        val maxDays = timeProvider.lastDayOfMonth(startOfMonth)
        return day in 1..maxDays
    }

    /**
     * Converts a valid 8-character date string into Epoch milliseconds.
     *
     * @param dateStr The complete and valid date string in "ddMMyyyy" format.
     * @return The corresponding Epoch milliseconds.
     */
    fun deriveMillis(dateStr: String): EpochMillis {
        val day = dateStr.substring(0, 2).toInt()
        val month = dateStr.substring(2, 4).toInt()
        val year = dateStr.substring(4, 8).toInt()

        val startOfMonth = timeProvider.startOfMonth(month, year)
        val targetDate = timeProvider.plusDays(startOfMonth, day - 1)
        return timeProvider.combineDateAndTime(targetDate, 0, 0)
    }

    /**
     * Formats Epoch milliseconds into an 8-character date string.
     *
     * @param millis The date in Epoch milliseconds.
     * @return The formatted string "ddMMyyyy".
     */
    fun formatMillis(millis: EpochMillis): String {
        val day = timeProvider.dayOfMonth(millis).toString().padStart(2, '0')
        val month = timeProvider.monthValue(millis).toString().padStart(2, '0')
        val year = timeProvider.yearValue(millis).toString()
        return "$day$month$year"
    }
}