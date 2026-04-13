package fr.abknative.outgo.wallet.api.usecase

import fr.abknative.outgo.wallet.api.model.presenter.ProjectedOperation
import kotlinx.coroutines.flow.Flow

/**
 * Provides a continuous, reactive stream of active operations for a given timeframe.
 * "Active" implies that softly deleted records are filtered out at the database level.
 */
interface ObserveActiveOperationsUseCase {

    /**
     * Observes all active operations for a specific wallet and projects them
     * onto the given month/year timeframe.
     */
    operator fun invoke(walletId: String, month: Int, year: Int): Flow<List<ProjectedOperation>>
}