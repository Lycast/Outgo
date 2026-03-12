package fr.abknative.outgo.outgoing.impl.usecase

import fr.abknative.outgo.outgoing.api.model.Outgoing
import fr.abknative.outgo.outgoing.api.repository.OutgoingRepository
import fr.abknative.outgo.outgoing.api.usecase.ObserveActiveOutgoingsUseCase
import kotlinx.coroutines.flow.Flow

internal class ObserveActiveOutgoingsUseCaseImpl(
    private val repository: OutgoingRepository
) : ObserveActiveOutgoingsUseCase {

    override fun invoke(month: Int): Flow<List<Outgoing>> {
        return repository.observeOutgoingsByMonth(month)
    }
}