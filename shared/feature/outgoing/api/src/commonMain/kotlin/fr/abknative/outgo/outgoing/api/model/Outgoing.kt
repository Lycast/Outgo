package fr.abknative.outgo.outgoing.api.model

import fr.abknative.outgo.core.api.EpochMillis
import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.outgoing.api.Recurrence

/**
 * Represents a single outgoing expense or bill.
 *
 * @property id Unique identifier of the expense.
 * @property budgetId The associated budget identifier.
 * @property name The display name of the expense.
 * @property amountInCents The expense amount, stored in cents to prevent precision loss.
 * @property recurrence The frequency of the expense.
 * @property dueDay The day of the month the expense is due.
 * @property dueMonth The specific month the expense is due (nullable, typically used for yearly recurrences).
 * @property createdAt Creation timestamp.
 * @property updatedAt Last modification timestamp.
 * @property isDeleted Flag for soft-deletion, used to resolve state during synchronization.
 * @property syncStatus The current synchronization state with the remote server.
 */
data class Outgoing(
    val id: String,
    val budgetId: String = "default",
    val name: String,
    val amountInCents: Long,
    val recurrence: Recurrence,
    val dueDay: Int,
    val dueMonth: Int? = null,

    // Métadonnées Offline-First
    val createdAt: EpochMillis,
    val updatedAt: EpochMillis,
    val isDeleted: Boolean = false,
    val syncStatus: SyncStatus = SyncStatus.PENDING_CREATE
)
