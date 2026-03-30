package fr.abknative.outgo.wallet.network.mapper

import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.wallet.api.model.Operation
import fr.abknative.outgo.wallet.api.model.operation.OperationType
import fr.abknative.outgo.wallet.api.model.operation.Recurrence
import fr.abknative.outgo.wallet.network.dto.OperationNetworkDto

// Réseau -> Domaine (PULL)
fun OperationNetworkDto.toDomain(): Operation = Operation(
    id = id,
    walletId = walletId,
    name = name,
    amountInCents = amountInCents,
    type = OperationType.fromString(type),
    recurrence = Recurrence.fromString(recurrence),
    startDate = startDate,
    endDate = endDate,
    createdAt = createdAt,
    updatedAt = updatedAt,
    deletedAt = deletedAt,
    syncStatus = SyncStatus.SYNCED
)

// Domaine -> Réseau (PUSH)
fun Operation.toNetworkDto(): OperationNetworkDto = OperationNetworkDto(
    id = id,
    walletId = walletId,
    name = name,
    amountInCents = amountInCents,
    type = type.name,
    recurrence = recurrence.name,
    startDate = startDate,
    endDate = endDate,
    createdAt = createdAt,
    updatedAt = updatedAt,
    deletedAt = deletedAt
)