package fr.abknative.outgo.outgoing.api.usecase

import fr.abknative.outgo.outgoing.api.model.Outgoing

interface CalculateRemainingToPayUseCase {
    operator fun invoke(outgoings: List<Outgoing>, selectedMonth: Int): Long
}