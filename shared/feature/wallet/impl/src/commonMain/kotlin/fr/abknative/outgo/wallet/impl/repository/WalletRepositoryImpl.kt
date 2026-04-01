package fr.abknative.outgo.wallet.impl.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import fr.abknative.outgo.core.api.AppDispatchers
import fr.abknative.outgo.core.api.IdProvider
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.core.api.logs.*
import fr.abknative.outgo.core.api.model.SyncStatus
import fr.abknative.outgo.database.OutgoDatabase
import fr.abknative.outgo.wallet.api.model.Wallet
import fr.abknative.outgo.wallet.api.repository.WalletRepository
import fr.abknative.outgo.wallet.impl.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * SQLDelight implementation of [WalletRepository].
 *
 * Uses [OutgoDatabase] as the local source of truth and handles
 * data mapping from database entities to domain models.
 */
internal class WalletRepositoryImpl(
    private val database: OutgoDatabase,
    private val dispatchers: AppDispatchers,
    private val timeProvider: TimeProvider,
    private val idProvider: IdProvider
) : WalletRepository {

    private val queries = database.walletQueries
    private val operationQueries = database.operationQueries
    private val tag = "WalletLocalRepo"

    override fun observeActiveWallets(): Flow<List<Wallet>> {
        return queries.getActiveWallets()
            .asFlow()
            .mapToList(dispatchers.io)
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getWalletById(id: String): Result<Wallet?, AppException> = asResult(
        onError = {
            AppLogger.get()?.e(tag, "Failed to fetch wallet with id: $id", it)
            CommonError.DatabaseError(it)
        }
    ) {
        queries.getWalletById(id).executeAsOneOrNull()?.toDomain()
    }

    override suspend fun save(wallet: Wallet): Result<Unit, AppException> = asResult(
        onError = {
            AppLogger.get()?.e(tag, "Failed to save wallet: ${wallet.id}", it)
            CommonError.DatabaseError(it)
        }
    ) {
        queries.transaction {
            val now = timeProvider.now()
            val existing = queries.getWalletById(wallet.id).executeAsOneOrNull()

            if (existing == null) {
                val finalId = wallet.id.ifBlank { idProvider.generate() }
                queries.insertWallet(
                    id = finalId,
                    name = wallet.name,
                    createdAt = now,
                    updatedAt = now,
                    deletedAt = null,
                    syncStatus = SyncStatus.PENDING_CREATE.name
                )
            } else {
                queries.updateWallet(
                    name = wallet.name,
                    updatedAt = now,
                    deletedAt = wallet.deletedAt,
                    syncStatus = SyncStatus.PENDING_UPDATE.name,
                    id = wallet.id
                )
            }
        }
    }

    override suspend fun markAsDeleted(id: String): Result<Unit, AppException> = asResult(
        onError = {
            AppLogger.get()?.e(tag, "Failed to mark wallet as deleted: $id", it)
            CommonError.DatabaseError(it)
        }
    ) {
        queries.transaction {
            val now = timeProvider.now()

            // Soft Delete of Wallet
            queries.markAsDeleted(
                deletedAt = now,
                updatedAt = now,
                id = id
            )

            // CASCADING Soft Delete of all operations
            operationQueries.softDeleteByWalletId(
                deletedAt = now,
                updatedAt = now,
                walletId = id
            )
        }
    }

    override suspend fun getPendingWallets(): Result<List<Wallet>, AppException> = asResult(
        onError = {
            AppLogger.get()?.e(tag, "Failed to fetch pending wallets", it)
            CommonError.DatabaseError(it)
        }
    ) {
        queries.getPendingWallets().executeAsList().map { it.toDomain() }
    }

    override suspend fun updateSyncStatus(
        id: String,
        status: SyncStatus
    ): Result<Unit, AppException> = asResult(
        onError = {
            AppLogger.get()?.e(tag, "Failed to update sync status ($status) for id: $id", it)
            CommonError.DatabaseError(it)
        }
    ) {
        queries.updateSyncStatus(syncStatus = status.name, id = id)
    }

    override suspend fun syncFromServer(wallets: List<Wallet>): Result<Unit, AppException> = asResult(
        onError = {
            AppLogger.get()?.e(tag, "Failed to sync ${wallets.size} wallets from server", it)
            CommonError.DatabaseError(it)
        }
    ) {
        queries.transaction {
            wallets.forEach { remoteWallet ->
                val exists = queries.getWalletById(remoteWallet.id).executeAsOneOrNull() != null
                if (exists) {
                    queries.updateWallet(
                        name = remoteWallet.name,
                        updatedAt = remoteWallet.updatedAt,
                        deletedAt = remoteWallet.deletedAt,
                        syncStatus = SyncStatus.SYNCED.name,
                        id = remoteWallet.id
                    )
                } else {
                    queries.insertWallet(
                        id = remoteWallet.id,
                        name = remoteWallet.name,
                        createdAt = remoteWallet.createdAt,
                        updatedAt = remoteWallet.updatedAt,
                        deletedAt = remoteWallet.deletedAt,
                        syncStatus = SyncStatus.SYNCED.name
                    )
                }
            }
        }
    }

    override suspend fun deleteAll(): Result<Unit, AppException> = asResult(
        onError = {
            AppLogger.get()?.e(tag, "Failed to delete all wallets", it)
            CommonError.DatabaseError(it)
        }
    ) {
        queries.deleteAll()
    }
}