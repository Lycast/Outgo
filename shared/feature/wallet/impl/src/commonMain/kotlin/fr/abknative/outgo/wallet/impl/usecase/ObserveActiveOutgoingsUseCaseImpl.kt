package fr.abknative.outgo.wallet.impl.usecase

import fr.abknative.outgo.wallet.api.model.Outgoing
import fr.abknative.outgo.wallet.api.repository.OutgoingRepository
import fr.abknative.outgo.wallet.api.usecase.ObserveActiveOutgoingsUseCase
import kotlinx.coroutines.flow.Flow

internal class ObserveActiveOutgoingsUseCaseImpl(
    private val repository: OutgoingRepository
) : ObserveActiveOutgoingsUseCase {

    override fun invoke(month: Int): Flow<List<Outgoing>> {
        return repository.observeOutgoingsByMonth(month)
    }
}