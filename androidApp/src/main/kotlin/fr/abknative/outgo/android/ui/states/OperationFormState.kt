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

    // Nouveaux événements pour notre champ date hybride
    data class UpdateDateString(val dateString: String) : OperationFormEvent
    data class UpdateDateMillis(val millis: EpochMillis) : OperationFormEvent
}

// --- ÉTAT UI LOCAL (Transient State) ---
class OperationFormState(
    val operationId: String? = null,
    val walletId: String,
    val timeProvider: TimeProvider,
    initialName: String = "",
    initialAmount: String = "",
    initialType: OperationType = OperationType.EXPENSE,
    initialRecurrence: Recurrence = Recurrence.UNIQUE, // Par défaut UNIQUE en V1
    initialDateMillis: EpochMillis? = null
) {
    var nameBuffer by mutableStateOf(initialName)
    var amountBuffer by mutableStateOf(initialAmount)
    var typeSelection by mutableStateOf(initialType)
    var recurrenceSelection by mutableStateOf(initialRecurrence)

    // On initialise le buffer avec la date passée en paramètre, ou la date du jour
    var dateBuffer by mutableStateOf(
        formatMillisToDateString(initialDateMillis ?: timeProvider.now(), timeProvider)
    )

    val isDateValid: Boolean
        get() {
            if (dateBuffer.length != 8) return false
            val day = dateBuffer.substring(0, 2).toIntOrNull() ?: return false
            val month = dateBuffer.substring(2, 4).toIntOrNull() ?: return false
            val year = dateBuffer.substring(4, 8).toIntOrNull() ?: return false

            if (month !in 1..12) return false
            if (year !in 2000..2100) return false

            val startOfMonth = timeProvider.startOfMonth(month, year)
            val maxDays = timeProvider.lastDayOfMonth(startOfMonth)
            return day in 1..maxDays
        }

    // NOUVEAU : Validation incrémentale en temps réel (pour l'UI)
    val isDateError: Boolean
        get() {
            // Pas d'erreur si le champ est vide
            if (dateBuffer.isEmpty()) return false

            // --- Vérification du JOUR (JJ) ---
            if (dateBuffer.isNotEmpty()) {
                val firstDigit = dateBuffer[0].digitToIntOrNull() ?: return true
                // Un jour ne peut pas commencer par 4, 5, 6, 7, 8 ou 9
                if (firstDigit > 3) return true
            }
            if (dateBuffer.length >= 2) {
                val day = dateBuffer.substring(0, 2).toIntOrNull() ?: return true
                if (day !in 1..31) return true
            }

            // --- Vérification du MOIS (MM) ---
            if (dateBuffer.length >= 3) {
                val thirdDigit = dateBuffer[2].digitToIntOrNull() ?: return true
                // Un mois ne peut pas commencer par 2, 3, etc. (seulement 0 ou 1)
                if (thirdDigit > 1) return true
            }
            if (dateBuffer.length >= 4) {
                val month = dateBuffer.substring(2, 4).toIntOrNull() ?: return true
                if (month !in 1..12) return true
            }

            // --- Vérification de l'ANNÉE (AAAA) ---
            if (dateBuffer.length >= 5) {
                val fifthDigit = dateBuffer[4].digitToIntOrNull() ?: return true
                // On bloque les années qui ne commencent pas par 2 (pour les années 2000+)
                if (fifthDigit != 2) return true
            }

            // Si on a 8 chiffres, on fait la vérification stricte finale (ex: 29 Février)
            if (dateBuffer.length == 8) {
                return !isDateValid
            }

            // Si tout semble correct en cours de frappe, on ne met pas en rouge !
            return false
        }

    val isValid: Boolean
        get() {
            val isNameValid = nameBuffer.isNotBlank()
            val amountDecimal = amountBuffer.replace(',', '.').toBigDecimalOrNull()
            val isAmountValid = amountDecimal != null && amountDecimal > BigDecimal.ZERO

            return isNameValid && isAmountValid && isDateValid
        }

    val amountInCents: Long
        get() = amountBuffer
            .toBigDecimalOrNull()
            ?.setScale(2, RoundingMode.HALF_UP)
            ?.movePointRight(2)
            ?.toLong() ?: 0L

    val startDate: EpochMillis
        get() {
            // Si la date est en cours de saisie et invalide, on renvoie "now" en sécurité
            if (!isDateValid) return timeProvider.now()

            val day = dateBuffer.substring(0, 2).toInt()
            val month = dateBuffer.substring(2, 4).toInt()
            val year = dateBuffer.substring(4, 8).toInt()

            val startOfMonth = timeProvider.startOfMonth(month, year)
            val targetDate = timeProvider.plusDays(startOfMonth, day - 1)

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

            // Mise à jour via le clavier (ex: "08042026")
            is OperationFormEvent.UpdateDateString -> dateBuffer = event.dateString

            // Mise à jour via le calendrier natif (on convertit les millis en "ddMMyyyy")
            is OperationFormEvent.UpdateDateMillis -> {
                dateBuffer = formatMillisToDateString(event.millis, timeProvider)
            }
        }
    }

    private fun formatMillisToDateString(millis: EpochMillis, timeProvider: TimeProvider): String {
        val day = timeProvider.dayOfMonth(millis).toString().padStart(2, '0')
        val month = timeProvider.monthValue(millis).toString().padStart(2, '0')
        val year = timeProvider.yearValue(millis).toString()
        return "$day$month$year"
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
    initialRecurrence: Recurrence = Recurrence.UNIQUE,
    initialDateMillis: EpochMillis? = null
): OperationFormState {

    return remember(
        operationId,
        walletId,
        initialName,
        initialAmount,
        initialType,
        initialRecurrence,
        initialDateMillis
    ) {
        OperationFormState(
            operationId = operationId,
            walletId = walletId,
            timeProvider = timeProvider,
            initialName = initialName,
            initialAmount = initialAmount,
            initialType = initialType,
            initialRecurrence = initialRecurrence,
            initialDateMillis = initialDateMillis
        )
    }
}