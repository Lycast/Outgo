package fr.abknative.outgo.outgoing.api.model

import fr.abknative.outgo.core.api.EpochMillis
import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.outgoing.api.BillingCycle

data class Outgoing(
    val id: String,
    val name: String,
    val amountInCents: Long,
    val cycle: BillingCycle,
    val billingDay: Int,
    val billingMonth: Int? = null,

    // Métadonnées Offline-First
    val createdAt: EpochMillis,
    val updatedAt: EpochMillis,
    val isDeleted: Boolean = false,
    val syncStatus: SyncStatus = SyncStatus.PENDING_CREATE
)
