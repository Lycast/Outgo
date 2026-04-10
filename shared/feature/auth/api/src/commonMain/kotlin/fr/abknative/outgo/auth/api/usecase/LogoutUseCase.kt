package fr.abknative.outgo.auth.api.usecase

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result

interface LogoutUseCase {
    suspend operator fun invoke(displayLocalData: Boolean): Result<Unit, AppException>
}