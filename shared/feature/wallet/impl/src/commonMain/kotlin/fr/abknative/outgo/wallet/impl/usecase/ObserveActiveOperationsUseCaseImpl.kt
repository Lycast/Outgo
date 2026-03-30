package fr.abknative.outgo.wallet.impl.usecase

import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.wallet.api.model.Operation
import fr.abknative.outgo.wallet.api.repository.OperationRepository
import fr.abknative.outgo.wallet.api.usecase.ObserveActiveOperationsUseCase
import kotlinx.coroutines.flow.Flow

internal class ObserveActiveOperationsUseCaseImpl(
    private val repository: OperationRepository,
    private val timeProvider: TimeProvider
) : ObserveActiveOperationsUseCase {

    override fun invoke(walletId: String, month: Int, year: Int): Flow<List<Operation>> {
        val from = timeProvider.startOfMonth(month, year)
        val to = timeProvider.endOfMonth(month, year)

        return repository.observeOperationsByPeriod(from = from, to = to)
    }
}