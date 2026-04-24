package fr.abknative.outgo.auth.impl.usecase

import fr.abknative.outgo.auth.api.provider.SessionProvider
import fr.abknative.outgo.auth.api.repository.AuthRepository
import fr.abknative.outgo.auth.api.usecase.WipeDataAndLogoutUseCase
import fr.abknative.outgo.core.api.LocalDataDowngrader
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.CommonError
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.core.api.time.TimeProvider
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
internal class WipeDataAndLogoutUseCaseImpl(
    private val authRepository: AuthRepository,
    private val sessionProvider: SessionProvider,
    private val timeProvider: TimeProvider,
    private val httpClient: HttpClient,
    private val downgraders: List<LocalDataDowngrader>
) : WipeDataAndLogoutUseCase {

    override suspend fun invoke(): Result<Unit, AppException> {
        val currentUserId = sessionProvider.getCurrentUserId()
        val targetLocalId = sessionProvider.getLastLocalId() ?: "local_${Uuid.random()}"

        try {
            val response = httpClient.delete("/user/me")
            if (!response.status.isSuccess()) {
                return Result.Error(CommonError.UnknownError(Exception("Failed to delete server data: ${response.status}")))
            }
        } catch (e: Exception) {
            return Result.Error(CommonError.NetworkError(e))
        }

        val now = timeProvider.now()
        downgraders.forEach {
            it.downgradeToLocal(firebaseId = currentUserId, newLocalId = targetLocalId, now = now)
        }

        sessionProvider.commitPersistentId(targetLocalId)
        return authRepository.logout()
    }
}