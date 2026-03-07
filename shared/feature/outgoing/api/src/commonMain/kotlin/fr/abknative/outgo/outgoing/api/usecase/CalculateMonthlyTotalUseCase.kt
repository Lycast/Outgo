package fr.abknative.outgo.outgoing.api.usecase

import kotlinx.coroutines.flow.Flow

interface CalculateMonthlyTotalUseCase {
    operator fun invoke(): Flow<Long>
}