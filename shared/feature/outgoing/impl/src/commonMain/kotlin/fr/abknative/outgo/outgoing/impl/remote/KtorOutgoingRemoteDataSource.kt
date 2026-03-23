package fr.abknative.outgo.outgoing.impl.remote

import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.core.api.CommonError
import fr.abknative.outgo.core.api.Result
import fr.abknative.outgo.outgoing.api.model.Budget
import fr.abknative.outgo.outgoing.api.model.Outgoing
import fr.abknative.outgo.outgoing.api.remote.OutgoingRemoteDataSource
import fr.abknative.outgo.outgoing.impl.OutgoContract
import fr.abknative.outgo.outgoing.impl.mapper.toDomain
import fr.abknative.outgo.outgoing.impl.mapper.toNetworkDto
import fr.abknative.outgo.outgoing.network.SyncPullResponse
import fr.abknative.outgo.outgoing.network.SyncPushRequest
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class KtorOutgoingRemoteDataSource(
    private val httpClient: HttpClient,
    private val baseUrl: String = "http://localhost:8080" // À ajuster selon ton environnement
) : OutgoingRemoteDataSource {

    override suspend fun pushData(
        outgoings: List<Outgoing>,
        budgets: List<Budget>
    ): Result<Unit, AppException> {
        return try {
            val request = SyncPushRequest(
                outgoings = outgoings.map { it.toNetworkDto() },
                budgets = budgets.map { it.toNetworkDto() }
            )

            httpClient.post("${baseUrl}${OutgoContract.PATH_SYNC_PUSH}") {
                contentType(ContentType.Application.Json)
                setBody(request)
                // On utilise notre Mock Auth "debug" pour l'instant
                header(HttpHeaders.Authorization, "${OutgoContract.Headers.BEARER_PREFIX} debug")
            }

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(CommonError.NetworkError(cause = e))
        }
    }

    override suspend fun pullData(since: Long): Result<Pair<List<Outgoing>, List<Budget>>, AppException> {
        return try {
            val response: SyncPullResponse = httpClient.get("${baseUrl}${OutgoContract.PATH_SYNC_PULL}") {
                parameter("since", since)
                header(HttpHeaders.Authorization, "${OutgoContract.Headers.BEARER_PREFIX} debug")
            }.body()

            val domainOutgoings = response.outgoings.map { it.toDomain() }
            val domainBudgets = response.budgets.map { it.toDomain() }

            Result.Success(domainOutgoings to domainBudgets)
        } catch (e: Exception) {
            Result.Error(CommonError.NetworkError(cause = e))
        }
    }
}