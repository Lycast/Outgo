package fr.abknative.outgo.auth.api.usecase

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result

interface LoginUseCase {
    suspend operator fun invoke(email: String, password: String): Result<Unit, AppException>
}