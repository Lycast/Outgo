package fr.abknative.outgo.auth.impl.usecase

import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.auth.api.usecase.LoginUseCase
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result

internal class LoginUseCaseImpl(private val repository: AuthRepository) : LoginUseCase {
    override suspend fun invoke(email: String, password: String): Result<Unit, AppException> {
        return repository.login(email, password)
    }
}