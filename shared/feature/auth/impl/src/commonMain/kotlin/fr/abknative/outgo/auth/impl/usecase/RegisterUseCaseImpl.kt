package fr.abknative.outgo.auth.impl.usecase

import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.auth.api.usecase.RegisterUseCase
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result

internal class RegisterUseCaseImpl(private val repository: AuthRepository) : RegisterUseCase {
    override suspend fun invoke(email: String, password: String): Result<Unit, AppException> {
        return repository.register(email, password)
    }
}