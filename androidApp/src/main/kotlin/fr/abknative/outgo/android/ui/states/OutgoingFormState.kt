package fr.abknative.outgo.android.ui.states

import androidx.compose.runtime.*
import fr.abknative.outgo.outgoing.api.BillingCycle

// --- ÉVÉNEMENTS UI (Brouillon) ---
sealed interface OutgoingFormEvent {
    data class UpdateName(val name: String) : OutgoingFormEvent
    data class UpdateAmount(val amount: String) : OutgoingFormEvent
    data class UpdateCycle(val cycle: BillingCycle) : OutgoingFormEvent
    data class UpdateBillingDay(val day: String) : OutgoingFormEvent
}

// --- ÉTAT UI LOCAL (Transient State) ---
class OutgoingFormState(
    initialName: String = "",
    initialAmount: String = "",
    initialCycle: BillingCycle = BillingCycle.MONTHLY,
    initialDay: String = ""
) {
    var nameBuffer by mutableStateOf(initialName)
        private set

    var amountBuffer by mutableStateOf(initialAmount)
        private set

    var cycleSelection by mutableStateOf(initialCycle)
        private set

    var billingDayBuffer by mutableStateOf(initialDay)
        private set

    val isValid: Boolean
        get() = nameBuffer.isNotBlank() &&
                amountBuffer.toDoubleOrNull() != null &&
                billingDayBuffer.toIntOrNull() in 1..31

    fun onEvent(event: OutgoingFormEvent) {
        when(event) {
            is OutgoingFormEvent.UpdateName -> nameBuffer = event.name
            is OutgoingFormEvent.UpdateAmount -> amountBuffer = event.amount
            is OutgoingFormEvent.UpdateCycle -> cycleSelection = event.cycle
            is OutgoingFormEvent.UpdateBillingDay -> billingDayBuffer = event.day
        }
    }
}

@Composable
fun rememberOutgoingFormState(
    initialName: String = "",
    initialAmount: String = "",
    initialCycle: BillingCycle = BillingCycle.MONTHLY,
    initialDay: String = ""
): OutgoingFormState {
    return remember(initialName, initialAmount, initialCycle, initialDay) {
        OutgoingFormState(initialName, initialAmount, initialCycle, initialDay)
    }
}