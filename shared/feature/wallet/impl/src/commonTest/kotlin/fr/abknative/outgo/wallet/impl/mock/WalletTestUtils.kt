package fr.abknative.outgo.wallet.impl.mock

import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.wallet.api.model.Operation
import fr.abknative.outgo.wallet.api.model.Recurrence

fun createOutgoing(
    id: String = "id-test",
    name: String = "Netflix",
    amount: Long = 1000,
    dueDay: Int = 10,
    syncStatus: SyncStatus = SyncStatus.PENDING_CREATE
) = Operation(
    id = id,
    budgetId = "default",
    name = name,
    amountInCents = amount,
    recurrence = Recurrence.MONTHLY,
    dueDay = dueDay,
    createdAt = 0L,
    updatedAt = 0L,
    syncStatus = syncStatus
)