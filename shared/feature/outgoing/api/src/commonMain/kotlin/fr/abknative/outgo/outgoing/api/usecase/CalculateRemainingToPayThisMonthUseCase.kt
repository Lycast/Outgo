package fr.abknative.outgo.outgoing.api.usecase

import kotlinx.coroutines.flow.Flow

interface CalculateRemainingToPayThisMonthUseCase {
    operator fun invoke(currentDayOfMonth: Int): Flow<Long>
}