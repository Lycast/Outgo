package fr.abknative.outgo.server.core.usecase

import fr.abknative.outgo.server.core.repository.BudgetRepository
import fr.abknative.outgo.server.core.repository.OutgoingRepository
import fr.abknative.outgo.server.core.repository.TransactionRunner
import fr.abknative.outgo.wallet.network.SyncPullResponse

class GetSyncPullUseCase(
    private val budgetRepository: BudgetRepository,
    private val outgoingRepository: OutgoingRepository,
    private val transactionRunner: TransactionRunner
) {
    /**
     * Récupère toutes les données modifiées depuis le timestamp [since].
     * * @param userId L'identifiant sécurisé de l'utilisateur (provenant du token).
     * @param since Le timestamp (en millisecondes) de la dernière synchronisation du mobile.
     * @return Un objet [SyncPullResponse] contenant les listes de données à renvoyer.
     */
    operator fun invoke(userId: String, since: Long): SyncPullResponse {
        return transactionRunner {
            val updatedBudgets = budgetRepository.getBudgetsSince(userId = userId, since = since)
            val updatedOutgoings = outgoingRepository.getOutgoingsSince(userId = userId, since = since)

            val currentServerTime = System.currentTimeMillis()

            SyncPullResponse(
                budgets = updatedBudgets,
                outgoings = updatedOutgoings,
                serverTimestamp = currentServerTime
            )
        }
    }
}