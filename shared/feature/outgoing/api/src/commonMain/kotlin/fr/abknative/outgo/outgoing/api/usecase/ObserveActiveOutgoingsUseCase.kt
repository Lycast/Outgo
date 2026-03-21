package fr.abknative.outgo.outgoing.api.usecase

import fr.abknative.outgo.outgoing.api.model.Outgoing
import kotlinx.coroutines.flow.Flow

/**
 * Provides a reactive stream of active outgoings for a specific month.
 * "Active" implies that softly deleted records are already filtered out.
 */
interface ObserveActiveOutgoingsUseCase {

    /**
     * Observes the flow of active expenses.
     *
     * @param month The target month (e.g., 1 for January, 12 for December).
     * @return A [Flow] emitting the updated list of [Outgoing] expenses.
     */
    operator fun invoke(month: Int): Flow<List<Outgoing>>
}