package fr.abknative.outgo.wallet.impl.usecase

import fr.abknative.outgo.wallet.api.model.Operation
import fr.abknative.outgo.wallet.api.repository.OutgoingRepository
import fr.abknative.outgo.wallet.api.usecase.ObserveActiveOperationsUseCase
import kotlinx.coroutines.flow.Flow

internal class ObserveActiveOperationsUseCaseImpl(
    private val repository: OutgoingRepository
) : ObserveActiveOperationsUseCase {

    override fun invoke(month: Int): Flow<List<Operation>> {
        return repository.observeOutgoingsByMonth(month)
    }
}