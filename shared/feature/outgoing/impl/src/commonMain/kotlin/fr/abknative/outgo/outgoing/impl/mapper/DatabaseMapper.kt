package fr.abknative.outgo.outgoing.impl.mapper

import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.database.OutgoingEntity
import fr.abknative.outgo.outgoing.api.model.Outgoing

internal fun OutgoingEntity.toDomain(): Outgoing {
    return Outgoing(
        id = this.id,
        name = this.name,
        amountInCents = this.amountInCents,
        cycle = mapToBillingCycle(this.cycle),
        billingDay = this.billingDay.toInt(),
        billingMonth = this.billingMonth?.toInt(),
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        isDeleted = this.isDeleted == 1L,
        syncStatus = SyncStatus.entries.find { it.name == this.syncStatus } ?: SyncStatus.UNKNOWN
    )
}