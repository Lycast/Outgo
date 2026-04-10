package fr.abknative.outgo.core.impl.usecase

import fr.abknative.outgo.core.api.DataPurger
import fr.abknative.outgo.core.api.KeyValueStorage
import fr.abknative.outgo.core.api.usecase.ClearLocalDataUseCase

/**
 * Implementation of [ClearLocalDataUseCase] that performs a complete
 * application reset by purging all feature data and clearing local storage.
 */
internal class ClearLocalDataUseCaseImpl(
    private val purgers: List<DataPurger>,
    private val storage: KeyValueStorage
) : ClearLocalDataUseCase {

    override suspend fun invoke() {
        purgers.forEach { purger ->
            purger.purgeData(userId = null)
        }
        storage.clearAll()
    }
}