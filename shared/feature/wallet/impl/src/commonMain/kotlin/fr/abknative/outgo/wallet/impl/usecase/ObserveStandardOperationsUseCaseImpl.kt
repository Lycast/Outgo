package fr.abknative.outgo.wallet.impl.usecase

import fr.abknative.outgo.wallet.api.model.Operation
import fr.abknative.outgo.wallet.api.repository.OperationRepository
import fr.abknative.outgo.wallet.api.usecase.ObserveStandardOperationsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ObserveStandardOperationsUseCaseImpl(
    private val repository: OperationRepository
) : ObserveStandardOperationsUseCase {

    override fun invoke(walletId: String): Flow<List<Operation>> {
        return repository.observeAllOperations(walletId)
            .map { operations ->
                operations.sortedByDescending { it.startDate }
            }
    }
}