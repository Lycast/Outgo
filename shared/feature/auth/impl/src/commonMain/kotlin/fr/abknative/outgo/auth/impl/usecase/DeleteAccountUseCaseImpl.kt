package fr.abknative.outgo.auth.impl.usecase

import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.auth.api.usecase.DeleteAccountUseCase
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.core.api.usecase.ClearLocalDataUseCase

internal class DeleteAccountUseCaseImpl(
    private val authRepository: AuthRepository,
    private val clearLocalDataUseCase: ClearLocalDataUseCase
) : DeleteAccountUseCase {

    override suspend fun invoke(): Result<Unit, AppException> {
        val networkResult = authRepository.deleteAccount()

        if (networkResult is Result.Success) {
            clearLocalDataUseCase()
        }

        return networkResult
    }
}