package fr.abknative.outgo.android.ui.states

import androidx.compose.runtime.*
import fr.abknative.outgo.outgoing.api.Recurrence

// --- ÉVÉNEMENTS UI ---
sealed interface OutgoingFormEvent {
    data class UpdateName(val name: String) : OutgoingFormEvent
    data class UpdateAmount(val amount: String) : OutgoingFormEvent
    data class UpdateRecurrence(val recurrence: Recurrence) : OutgoingFormEvent
    data class UpdateDueDay(val day: String) : OutgoingFormEvent
    data class UpdateDueMonth(val month: String) : OutgoingFormEvent
}

// --- ÉTAT UI LOCAL (Transient State) ---
class OutgoingFormState(
    val outgoingId: String? = null,
    initialName: String = "",
    initialAmount: String = "",
    initialRecurrence: Recurrence = Recurrence.MONTHLY,
    initialDueDay: String = "",
    initialDueMonth: String = ""
) {
    var nameBuffer by mutableStateOf(initialName)
    var amountBuffer by mutableStateOf(initialAmount)
    var recurrenceSelection by mutableStateOf(initialRecurrence)
    var dueDayBuffer by mutableStateOf(initialDueDay)
    var dueMonthBuffer by mutableStateOf(initialDueMonth)

    val isValid: Boolean
        get() {
            val isNameValid = nameBuffer.isNotBlank()
            val amountDecimal = amountBuffer.replace(',', '.').toBigDecimalOrNull()
            val isAmountValid = amountDecimal != null && amountDecimal > java.math.BigDecimal.ZERO
            val dayInt = dueDayBuffer.toIntOrNull()
            val isDayValid = dayInt != null && dayInt in 1..31

            val isMonthValid = if (recurrenceSelection == Recurrence.YEARLY) {
                val monthInt = dueMonthBuffer.toIntOrNull()
                monthInt != null && monthInt in 1..12
            } else true

            return isNameValid && isAmountValid && isDayValid && isMonthValid
        }

    val amountInCents: Long
        get() = amountBuffer
            .toBigDecimalOrNull()
            ?.setScale(2, java.math.RoundingMode.HALF_UP)
            ?.movePointRight(2)
            ?.toLong() ?: 0L

    fun onEvent(event: OutgoingFormEvent) {
        when(event) {
            is OutgoingFormEvent.UpdateName -> nameBuffer = event.name
            is OutgoingFormEvent.UpdateAmount -> {
                if (event.amount.length <= 12 && event.amount.all { it.isDigit() || it == '.' || it == ',' }) {
                    amountBuffer = event.amount.replace(',', '.')
                }
            }
            is OutgoingFormEvent.UpdateRecurrence -> {
                recurrenceSelection = event.recurrence
                if (event.recurrence == Recurrence.MONTHLY) dueMonthBuffer = ""
            }
            is OutgoingFormEvent.UpdateDueDay -> dueDayBuffer = event.day
            is OutgoingFormEvent.UpdateDueMonth -> dueMonthBuffer = event.month
        }
    }
}

@Composable
fun rememberOutgoingFormState(
    outgoingId: String? = null,
    initialName: String = "",
    initialAmount: String = "",
    initialRecurrence: Recurrence = Recurrence.MONTHLY,
    initialDueDay: String = "",
    initialDueMonth: String = ""
): OutgoingFormState {

    return remember(outgoingId, initialName, initialAmount, initialRecurrence, initialDueDay, initialDueMonth) {
        OutgoingFormState(
            outgoingId = outgoingId,
            initialName = initialName,
            initialAmount = initialAmount,
            initialRecurrence = initialRecurrence,
            initialDueDay = initialDueDay,
            initialDueMonth = initialDueMonth
        )
    }
}