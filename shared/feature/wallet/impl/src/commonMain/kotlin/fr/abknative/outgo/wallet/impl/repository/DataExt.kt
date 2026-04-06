package fr.abknative.outgo.wallet.impl.repository

import fr.abknative.outgo.core.api.IdProvider
import fr.abknative.outgo.core.api.model.SyncStatus
import fr.abknative.outgo.database.OperationQueries
import fr.abknative.outgo.database.WalletQueries
import fr.abknative.outgo.wallet.api.model.Operation
import fr.abknative.outgo.wallet.api.model.Wallet

/**
 * Maps a domain Operation to a SQL insert command.
 */
internal fun OperationQueries.insertFromDomain(
    op: Operation, uid: String, status: SyncStatus,
    createdAt: Long, updatedAt: Long, idProvider: IdProvider
) {
    this.insertOperation(
        id = op.id.ifBlank { idProvider.generate() }, userId = uid,
        walletId = op.walletId, name = op.name, amountInCents = op.amountInCents,
        type = op.type.name, recurrence = op.recurrence.name, startDate = op.startDate,
        endDate = op.endDate, createdAt = createdAt, updatedAt = updatedAt,
        deletedAt = op.deletedAt, syncStatus = status.name
    )
}

/**
 * Maps a domain Operation to a SQL update command.
 */
internal fun OperationQueries.updateFromDomain(
    op: Operation, uid: String, status: SyncStatus,
    updatedAt: Long, deletedAt: Long?
) {
    this.updateOperation(
        id = op.id, userId = uid, walletId = op.walletId,
        name = op.name, amountInCents = op.amountInCents, type = op.type.name,
        recurrence = op.recurrence.name, startDate = op.startDate, endDate = op.endDate,
        updatedAt = updatedAt, deletedAt = deletedAt, syncStatus = status.name
    )
}


/**
 * Maps a domain Wallet to a SQL insert command.
 */
internal fun WalletQueries.insertFromDomain(
    wallet: Wallet, uid: String, status: SyncStatus,
    createdAt: Long, updatedAt: Long, idProvider: IdProvider
) {
    this.insertWallet(
        id = wallet.id.ifBlank { idProvider.generate() }, userId = uid,
        name = wallet.name, createdAt = createdAt, updatedAt = updatedAt,
        deletedAt = wallet.deletedAt, syncStatus = status.name
    )
}

/**
 * Maps a domain Wallet to a SQL update command.
 */
internal fun WalletQueries.updateFromDomain(
    wallet: Wallet, uid: String, status: SyncStatus,
    updatedAt: Long, deletedAt: Long?
) {
    this.updateWallet(
        id = wallet.id, userId = uid, name = wallet.name,
        updatedAt = updatedAt, deletedAt = deletedAt, syncStatus = status.name
    )
}