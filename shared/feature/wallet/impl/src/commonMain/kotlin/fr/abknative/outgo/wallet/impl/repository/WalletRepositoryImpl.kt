package fr.abknative.outgo.wallet.impl.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import fr.abknative.outgo.auth.api.provider.SessionProvider
import fr.abknative.outgo.core.api.AppDispatchers
import fr.abknative.outgo.core.api.IdProvider
import fr.abknative.outgo.core.api.logs.*
import fr.abknative.outgo.core.api.model.SyncStatus
import fr.abknative.outgo.core.api.time.TimeProvider
import fr.abknative.outgo.database.OutgoDatabase
import fr.abknative.outgo.wallet.api.model.Wallet
import fr.abknative.outgo.wallet.api.repository.WalletRepository
import fr.abknative.outgo.wallet.impl.mapper.toDomain
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
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

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeActiveWallets(): Flow<List<Wallet>> {
        return sessionProvider.observeUserId()
            .flatMapLatest { uid ->
                queries.getActiveWallets(userId = uid)
                    .asFlow()
                    .mapToList(dispatchers.io)
                    .map { entities -> entities.map { it.toDomain() } }
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observePendingWallets(): Flow<List<Wallet>> {
        return sessionProvider.observeUserId()
            .flatMapLatest { uid ->
                queries.getPendingWallets(userId = uid)
                    .asFlow()
                    .mapToList(dispatchers.io)
                    .map { entities -> entities.map { it.toDomain() } }
            }
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
                    queries.insertFromDomain(wallet, uid, SyncStatus.PENDING_CREATE, now, now, idProvider)
                } else if (existing.name != wallet.name) {
                    val currentStatus = SyncStatus.fromString(existing.syncStatus)
                    val nextStatus = if (currentStatus == SyncStatus.PENDING_CREATE) SyncStatus.PENDING_CREATE else SyncStatus.PENDING_UPDATE

                    queries.updateFromDomain(wallet, uid, nextStatus, now, existing.deletedAt)
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

                val current = queries.getWalletById(id = id, userId = uid).executeAsOneOrNull()

                if (current?.syncStatus == SyncStatus.PENDING_CREATE.name) {
                    queries.hardDeletePendingCreate(id = id, userId = uid)
                } else {
                    queries.markAsDeleted(deletedAt = now, updatedAt = now, id = id, userId = uid)
                    operationQueries.softDeleteByWalletId(deletedAt = now, updatedAt = now, walletId = id, userId = uid)
                }
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
                    wallets.forEach { remote ->
                        val local = queries.getWalletById(id = remote.id, userId = uid).executeAsOneOrNull()

                        if (local == null) {
                            queries.insertFromDomain(remote, uid, SyncStatus.SYNCED, remote.createdAt, remote.updatedAt, idProvider)
                        } else {
                            val isServerNewer = remote.updatedAt > local.updatedAt
                            val isSynced = SyncStatus.fromString(local.syncStatus) == SyncStatus.SYNCED

                            if (isSynced || isServerNewer) {
                                queries.updateFromDomain(remote, uid, SyncStatus.SYNCED, remote.updatedAt, remote.deletedAt)
                            }
                        }
                    }
                }
            }
        }

    override suspend fun deleteAll(userId: String?): Result<Unit, AppException> = withContext(dispatchers.io) {
        asResult(
            onError = {
                AppLogger.get()?.e(tag, "Failed to delete all wallets", it)
                CommonError.DatabaseError(it)
            }
        ) {
            if (userId != null) {
                queries.deleteAllForUser(userId = userId)
            } else {
                queries.deleteAll()
            }
            Unit
        }
    }
}