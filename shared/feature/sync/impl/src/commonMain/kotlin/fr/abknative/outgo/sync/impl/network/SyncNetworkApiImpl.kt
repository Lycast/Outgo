package fr.abknative.outgo.sync.impl.network

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.ExceptionMapper
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
    private val httpClient: HttpClient,
    private val exceptionMapper: ExceptionMapper
) : SyncNetworkApi {

    override suspend fun pushData(request: SyncPushRequest): Result<Unit, AppException> = asResult(
        onError = { exceptionMapper.map(it) }
    ) {
        httpClient.post("/sync/push") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    override suspend fun pullData(since: Long): Result<SyncPullResponse, AppException> = asResult(
        onError = { exceptionMapper.map(it) }
    ) {
        val response = httpClient.get("/sync/pull") {
            parameter("since", since)
        }
        response.body()
    }
}