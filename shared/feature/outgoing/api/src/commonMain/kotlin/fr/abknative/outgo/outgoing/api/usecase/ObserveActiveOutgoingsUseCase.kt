package fr.abknative.outgo.outgoing.api.usecase

import fr.abknative.outgo.outgoing.api.model.Outgoing
import kotlinx.coroutines.flow.Flow

interface ObserveActiveOutgoingsUseCase {
    /**
     * Observe le flux des dépenses actives pour un mois donné.
     * @param month Le mois sélectionné (1-12).
     */
    operator fun invoke(month: Int): Flow<List<Outgoing>>
}