package fr.abknative.outgo.auth.impl.usecase

import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.auth.api.usecase.LogoutUseCase

internal class LogoutUseCaseImpl(private val repository: AuthRepository) : LogoutUseCase {
    override suspend fun invoke() = repository.logout()
}