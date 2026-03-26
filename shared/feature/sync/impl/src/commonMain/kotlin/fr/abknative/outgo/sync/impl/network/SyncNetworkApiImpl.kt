package fr.abknative.outgo.sync.impl.network

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.CommonError
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.core.api.logs.asResult
import fr.abknative.outgo.outgoing.network.SyncPullResponse
import fr.abknative.outgo.outgoing.network.SyncPushRequest
import fr.abknative.outgo.sync.api.SyncNetworkApi
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

internal class SyncNetworkApiImpl(
    private val httpClient: HttpClient
) : SyncNetworkApi {

    //todo extrait les exceptions
    override suspend fun pushData(request: SyncPushRequest): Result<Unit, AppException> = asResult(
        onError = { CommonError.NetworkError(it) }
    ) {
        val response = httpClient.post("/sync/push") {
            header(HttpHeaders.Authorization, "Bearer debug")
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        if (!response.status.isSuccess()) {
            throw Exception("Serveur injoignable ou erreur HTTP ${response.status.value}")
        }
    }

    override suspend fun pullData(since: Long): Result<SyncPullResponse, AppException> = asResult(
        onError = { CommonError.NetworkError(it) }
    ) {
        val response = httpClient.get("/sync/pull") {
            parameter("since", since)
        }

        if (!response.status.isSuccess()) {
            // L'exception sera capturée par `asResult` et castée en CommonError.NetworkError
            throw Exception("Serveur injoignable ou erreur HTTP ${response.status.value}")
        }

        // On ne tente de parser le body que si le serveur a répondu avec un code de succès
        response.body()
    }
}