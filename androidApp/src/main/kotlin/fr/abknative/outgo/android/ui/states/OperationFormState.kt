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
    data class UpdateMonth(val month: String) : OperationFormEvent // 👈 Nouveau
    data class UpdateYear(val year: String) : OperationFormEvent   // 👈 Nouveau
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
    initialDay: String = "",
    initialMonth: String = "",
    initialYear: String = ""
) {
    var nameBuffer by mutableStateOf(initialName)
    var amountBuffer by mutableStateOf(initialAmount)
    var typeSelection by mutableStateOf(initialType)
    var recurrenceSelection by mutableStateOf(initialRecurrence)

    var dayBuffer by mutableStateOf(initialDay)
    var monthBuffer by mutableStateOf(initialMonth)
    var yearBuffer by mutableStateOf(initialYear)

    val isValid: Boolean
        get() {
            val isNameValid = nameBuffer.isNotBlank()
            val amountDecimal = amountBuffer.replace(',', '.').toBigDecimalOrNull()
            val isAmountValid = amountDecimal != null && amountDecimal > BigDecimal.ZERO

            val dayInt = dayBuffer.toIntOrNull()
            val isDayValid = dayInt != null && dayInt in 1..31

            val monthInt = monthBuffer.toIntOrNull()
            val isMonthValid = monthInt != null && monthInt in 1..12

            val yearInt = yearBuffer.toIntOrNull()
            val isYearValid = yearInt != null && yearInt > 2000 // Sécurité basique

            return isNameValid && isAmountValid && isDayValid && isMonthValid && isYearValid
        }

    val amountInCents: Long
        get() = amountBuffer
            .toBigDecimalOrNull()
            ?.setScale(2, RoundingMode.HALF_UP)
            ?.movePointRight(2)
            ?.toLong() ?: 0L

    val startDate: EpochMillis
        get() {
            // On utilise les valeurs sélectionnées par l'utilisateur (ou celles en cours par défaut)
            val month = monthBuffer.toIntOrNull() ?: timeProvider.monthValue(timeProvider.now())
            val year = yearBuffer.toIntOrNull() ?: timeProvider.yearValue(timeProvider.now())

            val startOfMonth = timeProvider.startOfMonth(month, year)

            // Robustesse : On empêche les dates impossibles (ex: 31 Février)
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
            is OperationFormEvent.UpdateMonth -> monthBuffer = event.month // 👈 Nouveau
            is OperationFormEvent.UpdateYear -> yearBuffer = event.year    // 👈 Nouveau
        }
    }
}

@Composable
fun rememberOperationFormState(
    operationId: String? = null,
    walletId: String,
    timeProvider: TimeProvider,
    initialName: String = "",
    initialAmount: String = "",
    initialType: OperationType = OperationType.EXPENSE,
    initialRecurrence: Recurrence = Recurrence.MONTHLY,
    initialDay: String = "",
    initialMonth: String = "", // 👈 Nouveau
    initialYear: String = ""   // 👈 Nouveau
): OperationFormState {

    return remember(
        operationId,
        walletId,
        initialName,
        initialAmount,
        initialType,
        initialRecurrence,
        initialDay,
        initialMonth,
        initialYear
    ) {
        OperationFormState(
            operationId = operationId,
            walletId = walletId,
            timeProvider = timeProvider,
            initialName = initialName,
            initialAmount = initialAmount,
            initialType = initialType,
            initialRecurrence = initialRecurrence,
            initialDay = initialDay,
            initialMonth = initialMonth,
            initialYear = initialYear
        )
    }
}