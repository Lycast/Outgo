package fr.abknative.outgo.outgoing.impl.mapper

import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.database.BudgetEntity
import fr.abknative.outgo.database.OutgoingEntity
import fr.abknative.outgo.outgoing.api.model.Budget
import fr.abknative.outgo.outgoing.api.model.Outgoing

internal fun OutgoingEntity.toDomain(): Outgoing {
    return Outgoing(
        id = this.id,
        budgetId = this.budgetId,
        name = this.name,
        amountInCents = this.amountInCents,
        recurrence = mapToRecurrence(this.recurrence),
        dueDay = this.dueDay.toInt(),
        dueMonth = this.dueMonth?.toInt(),
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        isDeleted = this.isDeleted == 1L,
        syncStatus = SyncStatus.entries.find { it.name == this.syncStatus } ?: SyncStatus.UNKNOWN
    )
}

internal fun BudgetEntity.toDomain(): Budget {
    return Budget(
        id = this.id,
        monthlyIncomeInCents = this.monthlyIncomeInCents
    )
}