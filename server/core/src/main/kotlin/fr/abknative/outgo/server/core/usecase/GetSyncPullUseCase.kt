package fr.abknative.outgo.server.core.usecase

import fr.abknative.outgo.server.core.repository.OperationRepository
import fr.abknative.outgo.server.core.repository.TransactionRunner
import fr.abknative.outgo.server.core.repository.WalletRepository
import fr.abknative.outgo.wallet.network.SyncPullResponse

class GetSyncPullUseCase(
    private val walletRepository: WalletRepository,
    private val operationRepository: OperationRepository,
    private val transactionRunner: TransactionRunner
) {
    /**
     * Récupère toutes les données modifiées depuis le timestamp [since].
     *
     * @param userId L'identifiant sécurisé de l'utilisateur (provenant du token).
     * @param since Le timestamp (en millisecondes) de la dernière synchronisation du mobile.
     * @return Un objet [SyncPullResponse] contenant les listes de données à renvoyer.
     */
    operator fun invoke(userId: String, since: Long): SyncPullResponse {
        return transactionRunner {
            val updatedWallets = walletRepository.getWalletsSince(userId = userId, since = since)
            val updatedOperations = operationRepository.getOperationsSince(userId = userId, since = since)

            val currentServerTime = System.currentTimeMillis()

            SyncPullResponse(
                wallets = updatedWallets,
                operations = updatedOperations,
                serverTimestamp = currentServerTime
            )
        }
    }
}