package fr.abknative.outgo.outgoing.api.presenter

import fr.abknative.outgo.outgoing.api.BillingCycle

sealed interface OutgoingIntent {
    data class Save(
        val id: String? = null,
        val name: String,
        val amountInCents: Long,
        val cycle: BillingCycle,
        val billingDay: Int,
        val billingMonth: Int? = null
    ) : OutgoingIntent

    data class Delete(val id: String) : OutgoingIntent
    data class UpdateBudget(val amountInCents: Long) : OutgoingIntent
    object DismissError : OutgoingIntent
}