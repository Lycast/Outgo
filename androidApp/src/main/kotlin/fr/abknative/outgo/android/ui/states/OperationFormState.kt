package fr.abknative.outgo.android.ui.states

import androidx.compose.runtime.*
import fr.abknative.outgo.core.api.EpochMillis
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.wallet.api.model.operation.OperationType
import fr.abknative.outgo.wallet.api.model.operation.Recurrence
import java.math.BigDecimal
import java.math.RoundingMode

// --- ÉVÉNEMENTS UI ---
sealed interface OperationFormEvent {
    data class UpdateName(val name: String) : OperationFormEvent
    data class UpdateAmount(val amount: String) : OperationFormEvent
    data class UpdateType(val type: OperationType) : OperationFormEvent
    data class UpdateRecurrence(val recurrence: Recurrence) : OperationFormEvent
    data class UpdateDay(val day: String) : OperationFormEvent
}

// --- ÉTAT UI LOCAL (Transient State) ---
class OperationFormState(
    val operationId: String? = null,
    val walletId: String,
    val timeProvider: TimeProvider,
    initialName: String = "",
    initialAmount: String = "",
    initialType: OperationType = OperationType.EXPENSE,
    initialRecurrence: Recurrence = Recurrence.MONTHLY,
    initialDay: String = ""
) {
    var nameBuffer by mutableStateOf(initialName)
    var amountBuffer by mutableStateOf(initialAmount)
    var typeSelection by mutableStateOf(initialType)
    var recurrenceSelection by mutableStateOf(initialRecurrence)
    var dayBuffer by mutableStateOf(initialDay)

    val isValid: Boolean
        get() {
            val isNameValid = nameBuffer.isNotBlank()
            val amountDecimal = amountBuffer.replace(',', '.').toBigDecimalOrNull()
            val isAmountValid = amountDecimal != null && amountDecimal > BigDecimal.ZERO

            val dayInt = dayBuffer.toIntOrNull()
            val isDayValid = dayInt != null && dayInt in 1..31

            return isNameValid && isAmountValid && isDayValid
        }

    val amountInCents: Long
        get() = amountBuffer
            .toBigDecimalOrNull()
            ?.setScale(2, RoundingMode.HALF_UP)
            ?.movePointRight(2)
            ?.toLong() ?: 0L

    val startDate: EpochMillis
        get() {
            val now = timeProvider.now()
            val currentMonth = timeProvider.monthValue(now)
            val currentYear = timeProvider.yearValue(now)

            val startOfMonth = timeProvider.startOfMonth(currentMonth, currentYear)

            val maxDaysInMonth = timeProvider.lastDayOfMonth(startOfMonth)
            val rawDay = dayBuffer.toIntOrNull() ?: 1
            val safeDay = rawDay.coerceIn(1, maxDaysInMonth)

            val targetDate = timeProvider.plusDays(startOfMonth, safeDay - 1)

            return timeProvider.combineDateAndTime(
                dateEpochMillis = targetDate,
                hour = 0,
                minute = 0
            )
        }

    fun onEvent(event: OperationFormEvent) {
        when(event) {
            is OperationFormEvent.UpdateName -> nameBuffer = event.name
            is OperationFormEvent.UpdateAmount -> {
                if (event.amount.length <= 12 && event.amount.all { it.isDigit() || it == '.' || it == ',' }) {
                    amountBuffer = event.amount.replace(',', '.')
                }
            }
            is OperationFormEvent.UpdateType -> typeSelection = event.type
            is OperationFormEvent.UpdateRecurrence -> recurrenceSelection = event.recurrence
            is OperationFormEvent.UpdateDay -> dayBuffer = event.day
        }
    }
}

/**
 * Remembers and creates an instance of [OperationFormState].
 * Ensures the UI state survives recompositions while tracking the user's input.
 *
 * @param operationId The unique identifier of the operation if editing, or null for a new creation.
 * @param walletId The identifier of the parent wallet.
 * @param timeProvider The temporal engine dependency required to build exact timestamps.
 * @param initialName The initial text for the name field.
 * @param initialAmount The initial text for the amount field.
 * @param initialType The initial direction of the cash flow (Income or Expense).
 * @param initialRecurrence The initial cycle of the operation.
 * @param initialDay The initial selected day of the month (e.g., "15").
 */
@Composable
fun rememberOperationFormState(
    operationId: String? = null,
    walletId: String,
    timeProvider: TimeProvider,
    initialName: String = "",
    initialAmount: String = "",
    initialType: OperationType = OperationType.EXPENSE,
    initialRecurrence: Recurrence = Recurrence.MONTHLY,
    initialDay: String = ""
): OperationFormState {

    return remember(
        operationId,
        walletId,
        initialName,
        initialAmount,
        initialType,
        initialRecurrence,
        initialDay
    ) {
        OperationFormState(
            operationId = operationId,
            walletId = walletId,
            timeProvider = timeProvider,
            initialName = initialName,
            initialAmount = initialAmount,
            initialType = initialType,
            initialRecurrence = initialRecurrence,
            initialDay = initialDay
        )
    }
}