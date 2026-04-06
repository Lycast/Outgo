package fr.abknative.outgo.auth.impl.usecase

import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.auth.api.usecase.DeleteAccountUseCase
import fr.abknative.outgo.core.api.DataPurger
import fr.abknative.outgo.core.api.LocalDataDowngrader
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.CommonError
import fr.abknative.outgo.core.api.logs.Result
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

internal class DeleteAccountUseCaseImpl(
    private val authRepository: AuthRepository,
    private val httpClient: HttpClient,
    private val localDataPurgers: List<DataPurger>,
    private val downgraders: List<LocalDataDowngrader>
) : DeleteAccountUseCase {

    override suspend fun invoke(
        wipeLocal: Boolean,
        wipeServer: Boolean,
        revokeAuth: Boolean
    ): Result<Unit, AppException> {

        val shouldWipeServer = wipeServer || revokeAuth

        if (shouldWipeServer) {
            try {
                val response = httpClient.delete("/user/me")
                if (!response.status.isSuccess()) {
                    return Result.Error(CommonError.UnknownError(Exception("Failed to delete server data: ${response.status}")))
                }
                downgraders.forEach { it.downgradeAllToPendingCreate() }
            } catch (e: Exception) {
                return Result.Error(CommonError.UnknownError(e))
            }
        }

        if (revokeAuth) {
            val authResult = authRepository.deleteAccount()
            if (authResult is Result.Error) {
                return authResult
            }
        }

        if (wipeLocal) {
            try {
                localDataPurgers.forEach { it.purgeData() }
            } catch (e: Exception) {
                return Result.Error(CommonError.UnknownError(e))
            }
        } else if (revokeAuth) {
            authRepository.logout()
        }

        return Result.Success(Unit)
    }
}