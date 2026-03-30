package fr.abknative.outgo.wallet.network.mapper

import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.wallet.api.model.Operation
import fr.abknative.outgo.wallet.api.model.mapToRecurrence
import fr.abknative.outgo.wallet.network.dto.OutgoingNetworkDto

// Réseau -> Domaine (PULL)
fun OutgoingNetworkDto.toDomain(): Operation {
    return Operation(
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
fun Operation.toNetworkDto(): OutgoingNetworkDto {
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