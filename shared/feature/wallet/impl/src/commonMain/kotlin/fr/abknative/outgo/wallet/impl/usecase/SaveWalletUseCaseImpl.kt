package fr.abknative.outgo.wallet.impl.usecase

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.core.api.model.SyncStatus
import fr.abknative.outgo.wallet.api.logs.WalletError
import fr.abknative.outgo.wallet.api.model.Wallet
import fr.abknative.outgo.wallet.api.repository.WalletRepository
import fr.abknative.outgo.wallet.api.usecase.SaveWalletUseCase

internal class SaveWalletUseCaseImpl(
    private val repository: WalletRepository
) : SaveWalletUseCase {

    override suspend fun invoke(id: String?, name: String): Result<Unit, AppException> {

        val cleanName = name.trim()

        if (cleanName.isBlank()) { return Result.Error(WalletError.EmptyName()) }

        val walletToSave = Wallet(
            id = id ?: "",
            name = cleanName,
            createdAt = 0L,
            updatedAt = 0L,
            deletedAt = null,
            syncStatus = SyncStatus.PENDING_CREATE
        )

        return repository.save(walletToSave)
    }
}