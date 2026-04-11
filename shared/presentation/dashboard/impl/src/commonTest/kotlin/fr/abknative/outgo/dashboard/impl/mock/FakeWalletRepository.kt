package fr.abknative.outgo.dashboard.impl.mock

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.CommonError
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.core.api.model.SyncStatus
import fr.abknative.outgo.wallet.api.model.Wallet
import fr.abknative.outgo.wallet.api.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeWalletRepository : WalletRepository {
    private val _walletsFlow = MutableStateFlow<List<Wallet>>(emptyList())
    private val wallets = mutableMapOf<String, Wallet>()

    var shouldReturnError = false

    fun emit(wallet: Wallet) {
        wallets[wallet.id] = wallet
        _walletsFlow.value = wallets.values.toList()
    }

    override fun observeActiveWallets(): Flow<List<Wallet>> = _walletsFlow
    override fun observePendingWallets(): Flow<List<Wallet>> {
        TODO("Not yet implemented")
    }

    override suspend fun getWalletById(id: String): Result<Wallet?, AppException> {
        if (shouldReturnError) return Result.Error(CommonError.DatabaseError())
        return Result.Success(wallets[id])
    }

    override suspend fun save(wallet: Wallet): Result<Unit, AppException> {
        emit(wallet)
        return Result.Success(Unit)
    }

    override suspend fun markAsDeleted(id: String): Result<Unit, AppException> {
        val wallet = wallets[id]
        if (wallet != null) {
            emit(wallet.copy(deletedAt = 123456789L, syncStatus = SyncStatus.PENDING_DELETE))
        }
        return Result.Success(Unit)
    }

    override suspend fun getPendingWallets(): Result<List<Wallet>, AppException> {
        return Result.Success(wallets.values.filter { it.syncStatus != SyncStatus.SYNCED })
    }

    override suspend fun updateSyncStatus(id: String, status: SyncStatus): Result<Unit, AppException> {
        wallets[id]?.let { emit(it.copy(syncStatus = status)) }
        return Result.Success(Unit)
    }

    override suspend fun syncFromServer(wallets: List<Wallet>): Result<Unit, AppException> {
        wallets.forEach { emit(it.copy(syncStatus = SyncStatus.SYNCED)) }
        return Result.Success(Unit)
    }

    override suspend fun deleteAll(userId: String?): Result<Unit, AppException> {
        TODO("Not yet implemented")
    }
}