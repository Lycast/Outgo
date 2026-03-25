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

    override suspend fun pushData(request: SyncPushRequest): Result<Unit, AppException> = asResult(
        onError = { CommonError.NetworkError(it) }
    ) {
        httpClient.post("/sync/push") {
            header(HttpHeaders.Authorization, "Bearer debug")
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    override suspend fun pullData(since: Long): Result<SyncPullResponse, AppException> = asResult(
        onError = { CommonError.NetworkError(it) }
    ) {
        httpClient.get("/sync/pull") {
            parameter("since", since)
        }.body()
    }
}