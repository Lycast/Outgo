package fr.abknative.outgo.outgoing.api.usecase

import fr.abknative.outgo.outgoing.api.model.Outgoing

interface CalculateTotalOutgoingsUseCase {
    operator fun invoke(outgoings: List<Outgoing>): Long
}