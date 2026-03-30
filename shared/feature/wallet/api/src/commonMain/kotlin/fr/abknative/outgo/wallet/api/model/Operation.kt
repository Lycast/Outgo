package fr.abknative.outgo.wallet.api.model

import fr.abknative.outgo.core.api.EpochMillis
import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.wallet.api.model.operation.OperationType
import fr.abknative.outgo.wallet.api.model.operation.Recurrence

/**
 * Represents a single cash flow transaction, unifying both incomes and expenses.
 *
 * @property id The unique identifier (UUID v4) of the operation.
 * @property walletId The identifier of the [Wallet] this operation belongs to.
 * @property name The display name of the operation (e.g., "Salary", "Netflix").
 * @property amountInCents The monetary value, stored in cents to prevent floating-point precision loss.
 * @property type Indicates the direction of the cash flow ([fr.abknative.outgo.wallet.api.model.operation.OperationType.INCOME] or [fr.abknative.outgo.wallet.api.model.operation.OperationType.EXPENSE]).
 * @property recurrence Defines how frequently this operation repeats.
 * @property startDate The absolute temporal anchor (EpochMillis) marking when this operation begins or occurs.
 * @property endDate The absolute temporal anchor (EpochMillis) marking when a recurring operation ends. Null if indefinite or unique.
 * @property createdAt The local timestamp of initial creation.
 * @property updatedAt The local timestamp of the last modification.
 * @property deletedAt The timestamp indicating when this operation was soft-deleted. If null, the operation is active.
 * @property syncStatus The current synchronization state with the remote server.
 */
data class Operation(
    val id: String,
    val walletId: String,
    val name: String,
    val amountInCents: Long,
    val type: OperationType,
    val recurrence: Recurrence,

    // Moteur temporel absolu
    val startDate: EpochMillis,
    val endDate: EpochMillis? = null,

    // Métadonnées Offline-First
    val createdAt: EpochMillis,
    val updatedAt: EpochMillis,
    val deletedAt: EpochMillis? = null,
    val syncStatus: SyncStatus = SyncStatus.PENDING_CREATE
)
