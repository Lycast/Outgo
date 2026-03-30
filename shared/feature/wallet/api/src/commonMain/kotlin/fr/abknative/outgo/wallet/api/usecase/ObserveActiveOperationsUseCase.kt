package fr.abknative.outgo.wallet.api.usecase

import fr.abknative.outgo.wallet.api.model.Operation
import kotlinx.coroutines.flow.Flow

/**
 * Provides a continuous, reactive stream of active operations for a given timeframe.
 * "Active" implies that softly deleted records are filtered out at the database level.
 */
interface ObserveActiveOperationsUseCase {

    /**
     * Observes the cash flow operations.
     *
     * @param walletId The target wallet identifier.
     * @param month The target month (e.g., 1 for January, 12 for December).
     * @param year The target year (e.g., 2026), required to filter by absolute start dates.
     * @return A [Flow] emitting the updated list of [Operation]s whenever the underlying data changes.
     */
    operator fun invoke(walletId: String, month: Int, year: Int): Flow<List<Operation>>
}