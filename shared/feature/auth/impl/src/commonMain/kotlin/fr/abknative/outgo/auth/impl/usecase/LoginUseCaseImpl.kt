package fr.abknative.outgo.auth.impl.usecase

import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.auth.api.usecase.LoginUseCase
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.CommonError
import fr.abknative.outgo.core.api.logs.Result

internal class LoginUseCaseImpl(private val repository: AuthRepository) : LoginUseCase {
    override suspend fun invoke(email: String, password: String): Result<Unit, AppException> {
        return try {
            // Optionnel : tu pourrais ajouter une vérification de format ici
            // if (!email.contains("@")) return Result.Error(AuthError.InvalidEmailFormat)

            repository.login(email, password)
            Result.Success(Unit)
        } catch (e: Exception) {
            // todo On attrape l'erreur lancée par le Repository (identifiants incorrects) a ajouter a AuthError
            Result.Error(CommonError.UnknownError())
        }
    }
}