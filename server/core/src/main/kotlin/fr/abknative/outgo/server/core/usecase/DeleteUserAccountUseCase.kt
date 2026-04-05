package fr.abknative.outgo.server.core.usecase

import fr.abknative.outgo.server.core.repository.TransactionRunner
import fr.abknative.outgo.server.core.repository.UserRepository

/**
 * Orchestrates the user account deletion process.
 * Ensures the database operation is executed safely within a transaction.
 */
class DeleteUserAccountUseCase(
    private val userRepository: UserRepository,
    private val transactionRunner: TransactionRunner
) {
    operator fun invoke(userId: String) {
        transactionRunner {
            userRepository.deleteUser(userId)
        }
    }
}