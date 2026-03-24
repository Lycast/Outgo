package fr.abknative.outgo.server.core.usecase

import fr.abknative.outgo.outgoing.network.SyncPushRequest
import fr.abknative.outgo.server.core.repository.BudgetRepository
import fr.abknative.outgo.server.core.repository.OutgoingRepository
import fr.abknative.outgo.server.core.repository.TransactionRunner
import fr.abknative.outgo.server.core.repository.UserRepository

class ProcessSyncPushUseCase(
    private val userRepository: UserRepository,
    private val budgetRepository: BudgetRepository,
    private val outgoingRepository: OutgoingRepository,
    private val transactionRunner: TransactionRunner
) {
    operator fun invoke(userId: String, request: SyncPushRequest) {
        transactionRunner {
            userRepository.ensureUserExists(userId)
            request.budgets.forEach { budgetRepository.upsertFromDto(userId, it) }
            request.outgoings.forEach { outgoingRepository.upsertFromDto(userId, it) }
        }
    }
}