package fr.abknative.outgo.auth.impl.usecase

import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.auth.api.usecase.DeleteFirebaseAuthUseCase
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result

internal class DeleteFirebaseAuthUseCaseImpl(
    private val authRepository: AuthRepository
) : DeleteFirebaseAuthUseCase {

    override suspend fun invoke(): Result<Unit, AppException> {
        return authRepository.deleteAccount()
    }
}