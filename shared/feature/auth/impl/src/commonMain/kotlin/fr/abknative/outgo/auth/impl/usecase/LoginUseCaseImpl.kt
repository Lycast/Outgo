package fr.abknative.outgo.auth.impl.usecase

import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.auth.api.usecase.LoginUseCase
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.CommonError
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.core.api.logs.asResult

internal class LoginUseCaseImpl(private val repository: AuthRepository) : LoginUseCase {

    override suspend fun invoke(email: String, password: String): Result<Unit, AppException> = asResult(
        onError = { exception ->
            when (exception) {
                is AppException -> exception
                else -> CommonError.UnknownError(exception)
            }
        }
    ) {
        repository.login(email, password)
    }
}