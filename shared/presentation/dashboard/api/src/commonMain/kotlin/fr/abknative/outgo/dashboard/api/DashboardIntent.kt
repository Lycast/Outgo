package fr.abknative.outgo.dashboard.api

import fr.abknative.outgo.core.api.EpochMillis
import fr.abknative.outgo.wallet.api.model.operation.OperationType
import fr.abknative.outgo.wallet.api.model.operation.Recurrence

/**
 * Represents all possible user actions (intents) triggered from the Outgoing UI.
 * Follows the Unidirectional Data Flow (UDF) architecture.
 */
sealed interface DashboardIntent {

    data class Save(
        val id: String? = null,
        val walletId: String,
        val name: String,
        val amountInCents: Long,
        val type: OperationType,
        val recurrence: Recurrence,
        val startDate: EpochMillis,
        val endDate: EpochMillis? = null
    ) : DashboardIntent

    /** Intent to change the currently displayed month. */
    data class SelectMonth(val month: Int, val year: Int) : DashboardIntent

    /** Intent to soft-delete an expense by its [id]. */
    data class Delete(val id: String) : DashboardIntent

    /** Intent to expand or collapse the top summary (hero) section. */
    data class ToggleHeroSection(val isExpanded: Boolean) : DashboardIntent

    /** Intent to manually trigger a cloud synchronization. */
    object Refresh : DashboardIntent

    /** Intent to clear the current error state from the UI. */
    object DismissError : DashboardIntent
}