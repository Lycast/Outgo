package fr.abknative.outgo.outgoing.api.usecase

import kotlinx.coroutines.flow.Flow

interface CalculateTotalOutgoingsUseCase {
    operator fun invoke(): Flow<Long>
}