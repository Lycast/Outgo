package fr.abknative.outgo.wallet.impl.usecase

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.wallet.api.repository.WalletRepository
import fr.abknative.outgo.wallet.api.usecase.DeleteWalletUseCase

internal class DeleteWalletUseCaseImpl(
    private val repository: WalletRepository
) : DeleteWalletUseCase {
    override suspend fun invoke(id: String): Result<Unit, AppException> {
        return repository.markAsDeleted(id)
    }
}