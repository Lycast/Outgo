package fr.abknative.outgo.wallet.api.usecase

import fr.abknative.outgo.wallet.api.model.Wallet
import kotlinx.coroutines.flow.Flow

/**
 * Provides a reactive stream of all active wallets owned by the user.
 * It automatically filters out wallets that have been marked for deletion (soft-delete).
 */
interface ObserveWalletsUseCase {
    /**
     * Observes the collection of active wallets.
     *
     * @return A [Flow] emitting a list of [Wallet] entities, updated in real-time.
     */
    operator fun invoke(): Flow<List<Wallet>>
}