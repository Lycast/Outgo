package fr.abknative.outgo.core.impl.usecase

import fr.abknative.outgo.core.api.DataPurger
import fr.abknative.outgo.core.api.KeyValueStorage
import fr.abknative.outgo.core.api.usecase.ClearLocalDataUseCase

internal class ClearLocalDataUseCaseImpl(
    private val purgers: List<DataPurger>,
    private val storage: KeyValueStorage
) : ClearLocalDataUseCase {

    override suspend fun invoke() {
        purgers.forEach { purger ->
            purger.purgeData()
        }

        storage.clearAll()
    }
}