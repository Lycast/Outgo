package fr.abknative.outgo.android.ui.helpers

import androidx.compose.runtime.*
import fr.abknative.outgo.core.api.EpochMillis
import fr.abknative.outgo.core.api.TimeProvider

/**
 * A local UI manager that handles the complex logic of formatting
 * and validating a date string as the user types it ("ddMMyyyy").
 */
class DateInputManager(
    initialDate: EpochMillis,
    private val timeProvider: TimeProvider,
    private val onValidDateDerived: (EpochMillis) -> Unit
) {
    var textBuffer by mutableStateOf(formatMillisToDateString(initialDate))
        private set

    val isError: Boolean
        get() {
            if (textBuffer.isEmpty()) return false

            // --- Vérification incrémentale (comme tu l'avais fait) ---
            if (textBuffer.isNotEmpty()) {
                val firstDigit = textBuffer[0].digitToIntOrNull() ?: return true
                if (firstDigit > 3) return true
            }
            if (textBuffer.length >= 2) {
                val day = textBuffer.substring(0, 2).toIntOrNull() ?: return true
                if (day !in 1..31) return true
            }
            if (textBuffer.length >= 3) {
                val thirdDigit = textBuffer[2].digitToIntOrNull() ?: return true
                if (thirdDigit > 1) return true
            }
            if (textBuffer.length >= 4) {
                val month = textBuffer.substring(2, 4).toIntOrNull() ?: return true
                if (month !in 1..12) return true
            }
            if (textBuffer.length >= 5) {
                val fifthDigit = textBuffer[4].digitToIntOrNull() ?: return true
                if (fifthDigit != 2) return true
            }

            // Vérification stricte finale
            if (textBuffer.length == 8) {
                return !isDateValid(textBuffer)
            }
            return false
        }

    fun onTextChange(newText: String) {
        // N'accepte que les chiffres et limite à 8 caractères
        if (newText.length <= 8 && newText.all { it.isDigit() }) {
            textBuffer = newText

            // Si la date est complète et valide, on prévient le Presenter KMP
            if (textBuffer.length == 8 && isDateValid(textBuffer)) {
                val millis = deriveMillisFromValidString(textBuffer)
                onValidDateDerived(millis)
            }
        }
    }

    // Utilisé quand on sélectionne une date via un DatePicker natif
    fun onExternalDateSelected(millis: EpochMillis) {
        textBuffer = formatMillisToDateString(millis)
        onValidDateDerived(millis)
    }

    private fun isDateValid(dateStr: String): Boolean {
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

    private fun deriveMillisFromValidString(dateStr: String): EpochMillis {
        val day = dateStr.substring(0, 2).toInt()
        val month = dateStr.substring(2, 4).toInt()
        val year = dateStr.substring(4, 8).toInt()

        val startOfMonth = timeProvider.startOfMonth(month, year)
        val targetDate = timeProvider.plusDays(startOfMonth, day - 1)
        return timeProvider.combineDateAndTime(targetDate, 0, 0)
    }

    private fun formatMillisToDateString(millis: EpochMillis): String {
        val day = timeProvider.dayOfMonth(millis).toString().padStart(2, '0')
        val month = timeProvider.monthValue(millis).toString().padStart(2, '0')
        val year = timeProvider.yearValue(millis).toString()
        return "$day$month$year"
    }
}

@Composable
fun rememberDateInputManager(
    initialDate: EpochMillis,
    timeProvider: TimeProvider,
    onValidDateDerived: (EpochMillis) -> Unit
): DateInputManager {
    return remember(initialDate) {
        DateInputManager(initialDate, timeProvider, onValidDateDerived)
    }
}