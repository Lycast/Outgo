package fr.abknative.outgo.outgoing.api.model

import fr.abknative.outgo.core.api.EpochMillis
import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.outgoing.api.Recurrence

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
