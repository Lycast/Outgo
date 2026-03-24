package fr.abknative.outgo.outgoing.network.mapper

import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.outgoing.api.mapToRecurrence
import fr.abknative.outgo.outgoing.api.model.Outgoing
import fr.abknative.outgo.outgoing.network.dto.OutgoingNetworkDto

// Réseau -> Domaine (PULL)
fun OutgoingNetworkDto.toDomain(): Outgoing {
    return Outgoing(
        id = this.id,
        budgetId = this.budgetId,
        name = this.name,
        amountInCents = this.amountInCents,
        recurrence = mapToRecurrence(this.recurrence),
        dueDay = this.dueDay,
        dueMonth = this.dueMonth,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        isDeleted = this.isDeleted,
        syncStatus = SyncStatus.SYNCED
    )
}

// Domaine -> Réseau (PUSH)
fun Outgoing.toNetworkDto(): OutgoingNetworkDto {
    return OutgoingNetworkDto(
        id = this.id,
        budgetId = this.budgetId,
        name = this.name,
        amountInCents = this.amountInCents,
        recurrence = this.recurrence.name,
        dueDay = this.dueDay,
        dueMonth = this.dueMonth,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        isDeleted = this.isDeleted
    )
}