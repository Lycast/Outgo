package fr.abknative.outgo.auth.api.usecase

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result

interface LoginWithGoogleUseCase {
    suspend operator fun invoke(
        idToken: String,
        bypassMigration: Boolean = false
    ): Result<Unit, AppException>
}