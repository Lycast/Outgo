package fr.abknative.outgo.outgoing.api.presenter

import fr.abknative.outgo.outgoing.api.Recurrence

sealed interface OutgoingIntent {
    data class Save(
        val id: String? = null,
        val name: String,
        val amountInCents: Long,
        val recurrence: Recurrence,
        val dueDay: Int,
        val dueMonth: Int? = null
    ) : OutgoingIntent

    data class SelectMonth(val month: Int) : OutgoingIntent
    data class Delete(val id: String) : OutgoingIntent
    data class UpdateIncome(val amountInCents: Long) : OutgoingIntent
    data class ToggleHeroSection(val isExpanded: Boolean) : OutgoingIntent
    object DismissError : OutgoingIntent
}