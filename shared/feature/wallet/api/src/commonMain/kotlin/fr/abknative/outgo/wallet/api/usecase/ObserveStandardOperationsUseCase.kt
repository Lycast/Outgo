package fr.abknative.outgo.wallet.api.usecase

import fr.abknative.outgo.wallet.api.model.Operation
import kotlinx.coroutines.flow.Flow

/**
 * Retrieves the raw list of active operations (rules) for a given wallet.
 * This use case does NOT project recurrences into the future. It returns the exact
 * entities as they are stored in the database, representing the user's global configuration.
 */
interface ObserveStandardOperationsUseCase {
    operator fun invoke(walletId: String): Flow<List<Operation>>
}