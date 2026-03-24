package fr.abknative.outgo.server.core.usecase

import fr.abknative.outgo.outgoing.network.SyncPullResponse
import fr.abknative.outgo.server.core.repository.BudgetRepository
import fr.abknative.outgo.server.core.repository.OutgoingRepository

class GetSyncPullUseCase(
    private val budgetRepository: BudgetRepository,
    private val outgoingRepository: OutgoingRepository
) {
    /**
     * Récupère toutes les données modifiées depuis le timestamp [since].
     * * @param userId L'identifiant sécurisé de l'utilisateur (provenant du token).
     * @param since Le timestamp (en millisecondes) de la dernière synchronisation du mobile.
     * @return Un objet [SyncPullResponse] contenant les listes de données à renvoyer.
     */
    operator fun invoke(userId: String, since: Long): SyncPullResponse {

        // 1. On interroge les Repositories
        val updatedBudgets = budgetRepository.getBudgetsSince(userId = userId, since = since)
        val updatedOutgoings = outgoingRepository.getOutgoingsSince(userId = userId, since = since)

        val currentServerTime = System.currentTimeMillis()

        // 2. On assemble le colis pour le mobile
        return SyncPullResponse(
            budgets = updatedBudgets,
            outgoings = updatedOutgoings,
            serverTimestamp = currentServerTime
        )
    }
}