package fr.abknative.outgo.auth.impl.usecase

import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.auth.api.usecase.DeleteAccountUseCase
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result

internal class DeleteAccountUseCaseImpl(
    private val authRepository: AuthRepository
) : DeleteAccountUseCase {

    override suspend fun invoke(): Result<Unit, AppException> {
        val networkResult = authRepository.deleteAccount()

        return networkResult
    }
}