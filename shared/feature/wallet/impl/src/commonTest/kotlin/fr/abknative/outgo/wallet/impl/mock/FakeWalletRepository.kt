package fr.abknative.outgo.wallet.impl.mock

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.core.api.model.SyncStatus
import fr.abknative.outgo.wallet.api.model.Wallet
import fr.abknative.outgo.wallet.api.repository.WalletRepository
import kotlinx.coroutines.flow.Flow

class FakeWalletRepository : WalletRepository {
    var lastSavedWallet: Wallet? = null
    var walletToReturn: Wallet? = null
    var walletsToObserve: List<Wallet> = emptyList()

    override fun observeActiveWallets(): Flow<List<Wallet>> {
        return kotlinx.coroutines.flow.flowOf(walletsToObserve)
    }

    override fun observePendingWallets(): Flow<List<Wallet>> {
        TODO("Not yet implemented")
    }

    override suspend fun getWalletById(id: String): Result<Wallet?, AppException> {
        return Result.Success(walletToReturn)
    }

    override suspend fun save(wallet: Wallet): Result<Unit, AppException> {
        lastSavedWallet = wallet
        return Result.Success(Unit)
    }

    override suspend fun markAsDeleted(id: String): Result<Unit, AppException> = Result.Success(Unit)

    override suspend fun getPendingWallets(): Result<List<Wallet>, AppException> = Result.Success(emptyList())

    override suspend fun updateSyncStatus(id: String, status: SyncStatus): Result<Unit, AppException> = Result.Success(Unit)

    override suspend fun syncFromServer(wallets: List<Wallet>): Result<Unit, AppException> = Result.Success(Unit)
    override suspend fun deleteAll(userId: String?): Result<Unit, AppException> {
        TODO("Not yet implemented")
    }

}