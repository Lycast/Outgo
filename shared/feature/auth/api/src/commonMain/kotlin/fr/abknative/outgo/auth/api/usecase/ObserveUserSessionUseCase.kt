package fr.abknative.outgo.auth.api.usecase

import fr.abknative.outgo.auth.api.model.UserSession
import kotlinx.coroutines.flow.Flow

interface ObserveUserSessionUseCase {
    operator fun invoke(): Flow<UserSession?>
}