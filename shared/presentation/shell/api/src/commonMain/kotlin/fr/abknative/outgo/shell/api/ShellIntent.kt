package fr.abknative.outgo.shell.api

import fr.abknative.outgo.core.api.EpochMillis
import fr.abknative.outgo.wallet.api.model.operation.OperationType
import fr.abknative.outgo.wallet.api.model.operation.Recurrence

sealed interface ShellIntent {

    /** Demande l'ouverture du formulaire global. Si null, c'est une création. */
    data class OpenOperationForm(
        val operationId: String? = null,
        val name: String = "",
        val amount: String = "",
        val type: OperationType = OperationType.EXPENSE,
        val recurrence: Recurrence = Recurrence.UNIQUE,
        val startDate: EpochMillis? = null,
        val endDate: EpochMillis? = null
    ) : ShellIntent

    /** Ferme le formulaire global */
    data object CloseOperationForm : ShellIntent

    /** Intent to manually trigger a cloud synchronization from the global header. */
    object RefreshSync : ShellIntent

    /** Intent to clear any global error message. */
    object DismissError : ShellIntent
}