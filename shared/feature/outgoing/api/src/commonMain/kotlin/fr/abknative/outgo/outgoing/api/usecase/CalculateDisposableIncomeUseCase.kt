package fr.abknative.outgo.outgoing.api.usecase

import kotlinx.coroutines.flow.Flow

interface CalculateDisposableIncomeUseCase {
    /**
     * Calculates the "remaining income" (Monthly income - Total smoothed expenses).
     */
    operator fun invoke(): Flow<Long>
}