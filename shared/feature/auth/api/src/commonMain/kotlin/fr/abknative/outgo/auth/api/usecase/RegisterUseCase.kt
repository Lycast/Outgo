package fr.abknative.outgo.auth.api.usecase

import fr.abknative.outgo.auth.api.model.ConflictStrategy
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result

interface RegisterUseCase {
    suspend operator fun invoke(
        email: String,
        password: String,
        conflictStrategy: ConflictStrategy? = null
    ): Result<Unit, AppException>
}