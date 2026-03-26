package fr.abknative.outgo.auth.impl.usecase

import fr.abknative.outgo.auth.api.model.UserSession
import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.auth.api.usecase.ObserveUserSessionUseCase
import kotlinx.coroutines.flow.Flow

internal class ObserveUserSessionUseCaseImpl(private val repository: AuthRepository) : ObserveUserSessionUseCase {
    override fun invoke(): Flow<UserSession?> = repository.observeSession()
}