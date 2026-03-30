package fr.abknative.outgo.wallet.impl.usecase

import fr.abknative.outgo.wallet.api.model.Wallet
import fr.abknative.outgo.wallet.api.repository.WalletRepository
import fr.abknative.outgo.wallet.api.usecase.ObserveWalletsUseCase
import kotlinx.coroutines.flow.Flow

internal class ObserveWalletsUseCaseImpl(
    private val repository: WalletRepository
) : ObserveWalletsUseCase {
    override fun invoke(): Flow<List<Wallet>> {
        return repository.observeActiveWallets()
    }
}