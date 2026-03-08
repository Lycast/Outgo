package fr.abknative.outgo.outgoing.api.usecase

import kotlinx.coroutines.flow.Flow

interface CalculateRemainingToPayThisMonthUseCase {
    /**
     * Calculates the total outstanding balance for the current month.
     * Accounts for monthly subscriptions and any annual subscriptions due this month.
     */
    operator fun invoke(): Flow<Long>
}