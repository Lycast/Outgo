package fr.abknative.outgo.sync.impl.network

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.CommonError
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.core.api.logs.asResult
import fr.abknative.outgo.sync.api.SyncNetworkApi
import fr.abknative.outgo.wallet.network.SyncPullResponse
import fr.abknative.outgo.wallet.network.SyncPushRequest
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

internal class SyncNetworkApiImpl(
    private val httpClient: HttpClient
) : SyncNetworkApi {

    /**
     * Identifie la nature de l'erreur pour la relayer correctement à l'UI.
     */
    private fun mapToAppError(exception: Exception): AppException {
        return when (exception) {
            is AppException -> exception
            else -> CommonError.NetworkError(exception)
        }
    }

    /**
     * Valide le code HTTP et lève l'exception métier correspondante si nécessaire.
     */
    private fun validateResponse(status: HttpStatusCode) {
        when {
            status.isSuccess() -> return
            status == HttpStatusCode.Unauthorized || status == HttpStatusCode.Forbidden -> {
                throw CommonError.Unauthorized()
            }
            else -> {
                throw CommonError.ServerError()
            }
        }
    }

    override suspend fun pushData(request: SyncPushRequest): Result<Unit, AppException> = asResult(
        onError = ::mapToAppError
    ) {
        val response = httpClient.post("/sync/push") {
            header(HttpHeaders.Authorization, "Bearer debug")
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        validateResponse(response.status)
    }

    override suspend fun pullData(since: Long): Result<SyncPullResponse, AppException> = asResult(
        onError = ::mapToAppError
    ) {
        val response = httpClient.get("/sync/pull") {
            parameter("since", since)
        }

        validateResponse(response.status)
        response.body()
    }
}