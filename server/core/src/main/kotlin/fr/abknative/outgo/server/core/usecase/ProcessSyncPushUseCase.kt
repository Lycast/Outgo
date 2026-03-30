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
    operator fun invoke(userId: String, request: SyncPushRequest) {
        transactionRunner {
            userRepository.ensureUserExists(userId)
            request.wallets.forEach { walletRepository.upsertFromDto(userId, it) }
            request.operations.forEach { operationRepository.upsertFromDto(userId, it) }
        }
    }
}