package fr.abknative.outgo.outgoing.api.usecase

import fr.abknative.outgo.outgoing.api.model.Outgoing
import kotlinx.coroutines.flow.Flow

interface ObserveActiveOutgoingsUseCase {
    operator fun invoke(): Flow<List<Outgoing>>
}