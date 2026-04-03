package fr.abknative.outgo.wallet.impl.mock

import fr.abknative.outgo.core.api.model.SyncStatus
import fr.abknative.outgo.wallet.api.model.Operation
import fr.abknative.outgo.wallet.api.model.operation.OperationType
import fr.abknative.outgo.wallet.api.model.operation.Recurrence

fun createOp(name: String = "Test", amount: Long, day: Int): Operation {
    return Operation(
        id = "id", walletId = "w1", name = name, amountInCents = amount,
        type = OperationType.EXPENSE, recurrence = Recurrence.MONTHLY,
        startDate = day.toLong(),
        createdAt = 0, updatedAt = 0, syncStatus = SyncStatus.SYNCED, deletedAt = null, endDate = null
    )
}