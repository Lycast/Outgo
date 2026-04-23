package fr.abknative.outgo.auth.api.usecase

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result

interface LoginWithAppleUseCase {
    suspend operator fun invoke(
        idToken: String
    ): Result<Unit, AppException>
}