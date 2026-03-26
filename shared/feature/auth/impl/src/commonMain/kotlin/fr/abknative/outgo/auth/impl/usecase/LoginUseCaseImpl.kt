package fr.abknative.outgo.auth.impl.usecase

import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.auth.api.usecase.LoginUseCase

internal class LoginUseCaseImpl(private val repository: AuthRepository) : LoginUseCase {
    override suspend fun invoke() = repository.login()
}