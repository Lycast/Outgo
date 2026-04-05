package fr.abknative.outgo.server.core.usecase

import fr.abknative.outgo.server.core.repository.OperationRepository
import fr.abknative.outgo.server.core.repository.TransactionRunner
import fr.abknative.outgo.server.core.repository.UserRepository
import fr.abknative.outgo.server.core.repository.WalletRepository
import fr.abknative.outgo.wallet.network.SyncPushRequest

class ProcessSyncPushUseCase(
    private val userRepository: UserRepository,
    private val walletRepository: WalletRepository,
    private val operationRepository: OperationRepository,
    private val transactionRunner: TransactionRunner
) {
    /**
     * Processes incoming sync data from the mobile client.
     * Ensures the user exists in the database with their latest email.
     */
    operator fun invoke(userId: String, email: String, request: SyncPushRequest) {
        transactionRunner {
            userRepository.ensureUserExists(userId, email)

            request.wallets.forEach { walletRepository.upsertFromDto(userId, it) }
            request.operations.forEach { operationRepository.upsertFromDto(userId, it) }
        }
    }
}