package fr.abknative.outgo.outgoing.api.presenter

import fr.abknative.outgo.outgoing.api.Recurrence

/**
 * Represents all possible user actions (intents) triggered from the Outgoing UI.
 * Follows the Unidirectional Data Flow (UDF) architecture.
 */
sealed interface OutgoingIntent {

    /**
     * Intent to create a new expense or update an existing one.
     * * @property id The expense ID if updating, or null for a new creation.
     */
    data class Save(
        val id: String? = null,
        val name: String,
        val amountInCents: Long,
        val recurrence: Recurrence,
        val dueDay: Int,
        val dueMonth: Int? = null
    ) : OutgoingIntent

    /** Intent to change the currently displayed month. */
    data class SelectMonth(val month: Int) : OutgoingIntent

    /** Intent to soft-delete an expense by its [id]. */
    data class Delete(val id: String) : OutgoingIntent

    /** Intent to update the user's overall monthly income. */
    data class UpdateIncome(val amountInCents: Long) : OutgoingIntent

    /** Intent to expand or collapse the top summary (hero) section. */
    data class ToggleHeroSection(val isExpanded: Boolean) : OutgoingIntent

    /** Intent to clear the current error state from the UI. */
    object DismissError : OutgoingIntent
}