package fr.abknative.outgo.outgoing.api.presenter

import fr.abknative.outgo.outgoing.api.BillingCycle

sealed interface OutgoingIntent {
    data class Add(
        val name: String,
        val amountInCents: Long,
        val cycle: BillingCycle,
        val billingDay: Int
    ) : OutgoingIntent

    data class Delete(val id: String) : OutgoingIntent

    object DismissError : OutgoingIntent
}