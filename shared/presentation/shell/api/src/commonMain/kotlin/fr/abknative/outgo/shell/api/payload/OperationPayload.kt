package fr.abknative.outgo.shell.api.payload

import fr.abknative.outgo.core.api.time.EpochMillis
import fr.abknative.outgo.wallet.api.model.operation.OperationType
import fr.abknative.outgo.wallet.api.model.operation.Recurrence

/**
 * A data transfer object representing the initial state of the operation form.
 * If instantiated with default values, it represents a creation.
 * If instantiated with existing data, it represents an edition.
 */
data class OperationPayload(
    val id: String? = null,
    val name: String = "",
    val amount: String = "",
    val type: OperationType = OperationType.EXPENSE,
    val recurrence: Recurrence = Recurrence.UNIQUE,
    val startDate: EpochMillis? = null,
    val endDate: EpochMillis? = null
)