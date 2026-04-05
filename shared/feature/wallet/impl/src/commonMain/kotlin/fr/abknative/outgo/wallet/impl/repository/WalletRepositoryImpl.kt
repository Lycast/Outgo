package fr.abknative.outgo.wallet.impl.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import fr.abknative.outgo.auth.api.provider.SessionProvider
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
import kotlinx.coroutines.withContext

/**
 * SQLDelight implementation of [WalletRepository].
 *
 * Uses [OutgoDatabase] as the local source of truth and handles
 * data mapping from database entities to domain models.
 * Enforces data isolation by scoping all queries to the current user's ID.
 */
internal class WalletRepositoryImpl(
    private val database: OutgoDatabase,
    private val dispatchers: AppDispatchers,
    private val timeProvider: TimeProvider,
    private val idProvider: IdProvider,
    private val sessionProvider: SessionProvider
) : WalletRepository {

    private val queries = database.walletQueries
    private val operationQueries = database.operationQueries
    private val tag = "WalletLocalRepo"

    private val currentUserId: String
        get() = sessionProvider.getCurrentUserId()

    override fun observeActiveWallets(): Flow<List<Wallet>> {
        return queries.getActiveWallets(userId = currentUserId)
            .asFlow()
            .mapToList(dispatchers.io)
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getWalletById(id: String): Result<Wallet?, AppException> = withContext(dispatchers.io) {
        asResult(
            onError = {
                AppLogger.get()?.e(tag, "Failed to fetch wallet with id: $id", it)
                CommonError.DatabaseError(it)
            }
        ) {
            queries.getWalletById(id = id, userId = currentUserId).executeAsOneOrNull()?.toDomain()
        }
    }

    override suspend fun save(wallet: Wallet): Result<Unit, AppException> = withContext(dispatchers.io) {
        asResult(
            onError = {
                AppLogger.get()?.e(tag, "Failed to save wallet: ${wallet.id}", it)
                CommonError.DatabaseError(it)
            }
        ) {
            queries.transaction {
                val now = timeProvider.now()
                val uid = currentUserId
                val existing = queries.getWalletById(id = wallet.id, userId = uid).executeAsOneOrNull()

                if (existing == null) {
                    val finalId = wallet.id.ifBlank { idProvider.generate() }
                    queries.insertWallet(
                        id = finalId,
                        userId = uid,
                        name = wallet.name,
                        createdAt = now,
                        updatedAt = now,
                        deletedAt = null,
                        syncStatus = SyncStatus.PENDING_CREATE.name
                    )
                } else {
                    val currentStatus = SyncStatus.fromString(existing.syncStatus)
                    val nextStatus = if (currentStatus == SyncStatus.PENDING_CREATE) {
                        SyncStatus.PENDING_CREATE
                    } else {
                        SyncStatus.PENDING_UPDATE
                    }

                    if (existing.name != wallet.name) {
                        queries.updateWallet(
                            name = wallet.name,
                            updatedAt = now,
                            deletedAt = existing.deletedAt,
                            syncStatus = nextStatus.name,
                            id = wallet.id,
                            userId = uid
                        )
                    }
                }
            }
        }
    }

    override suspend fun markAsDeleted(id: String): Result<Unit, AppException> = withContext(dispatchers.io) {
        asResult(
            onError = {
                AppLogger.get()?.e(tag, "Failed to mark wallet as deleted: $id", it)
                CommonError.DatabaseError(it)
            }
        ) {
            queries.transaction {
                val now = timeProvider.now()
                val uid = currentUserId

                // Soft Delete of Wallet
                queries.markAsDeleted(
                    deletedAt = now,
                    updatedAt = now,
                    id = id,
                    userId = uid
                )

                // Cascading Soft Delete of Operations
                operationQueries.softDeleteByWalletId(
                    deletedAt = now,
                    updatedAt = now,
                    walletId = id,
                    userId = uid
                )
            }
        }
    }

    override suspend fun getPendingWallets(): Result<List<Wallet>, AppException> = withContext(dispatchers.io) {
        asResult(
            onError = {
                AppLogger.get()?.e(tag, "Failed to fetch pending wallets", it)
                CommonError.DatabaseError(it)
            }
        ) {
            queries.getPendingWallets(userId = currentUserId).executeAsList().map { it.toDomain() }
        }
    }

    override suspend fun updateSyncStatus(
        id: String,
        status: SyncStatus
    ): Result<Unit, AppException> = withContext(dispatchers.io) {
        asResult(
            onError = {
                AppLogger.get()?.e(tag, "Failed to update sync status ($status) for id: $id", it)
                CommonError.DatabaseError(it)
            }
        ) {
            queries.updateSyncStatus(syncStatus = status.name, id = id, userId = currentUserId)
            Unit
        }
    }

    override suspend fun syncFromServer(wallets: List<Wallet>): Result<Unit, AppException> =
        withContext(dispatchers.io) {
            asResult(
                onError = {
                    AppLogger.get()?.e(tag, "Failed to sync ${wallets.size} wallets from server", it)
                    CommonError.DatabaseError(it)
                }
            ) {
                queries.transaction {
                    val uid = currentUserId
                    wallets.forEach { remoteWallet ->
                        val exists = queries.getWalletById(id = remoteWallet.id, userId = uid).executeAsOneOrNull() != null
                        if (exists) {
                            queries.updateWallet(
                                name = remoteWallet.name,
                                updatedAt = remoteWallet.updatedAt,
                                deletedAt = remoteWallet.deletedAt,
                                syncStatus = SyncStatus.SYNCED.name,
                                id = remoteWallet.id,
                                userId = uid
                            )
                        } else {
                            queries.insertWallet(
                                id = remoteWallet.id,
                                userId = uid,
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
        }

    override suspend fun deleteAll(): Result<Unit, AppException> = withContext(dispatchers.io) {
        asResult(
            onError = {
                AppLogger.get()?.e(tag, "Failed to delete all wallets", it)
                CommonError.DatabaseError(it)
            }
        ) {
            queries.deleteAllForUser(userId = currentUserId)
            Unit
        }
    }
}