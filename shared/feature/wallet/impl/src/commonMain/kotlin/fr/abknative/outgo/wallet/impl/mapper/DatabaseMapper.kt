package fr.abknative.outgo.wallet.impl.mapper


import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.database.OperationEntity
import fr.abknative.outgo.database.WalletEntity
import fr.abknative.outgo.wallet.api.model.Operation
import fr.abknative.outgo.wallet.api.model.OperationType
import fr.abknative.outgo.wallet.api.model.Recurrence
import fr.abknative.outgo.wallet.api.model.Wallet

internal fun OperationEntity.toDomain(): Operation = Operation(
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
    syncStatus = SyncStatus.fromString(syncStatus)
)

internal fun WalletEntity.toDomain(): Wallet = Wallet(
    id = id,
    name = name,
    createdAt = createdAt,
    updatedAt = updatedAt,
    deletedAt = deletedAt,
    syncStatus = SyncStatus.fromString(syncStatus)
)