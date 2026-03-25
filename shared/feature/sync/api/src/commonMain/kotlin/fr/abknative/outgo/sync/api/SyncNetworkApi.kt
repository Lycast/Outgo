package fr.abknative.outgo.sync.api

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.outgoing.network.SyncPullResponse
import fr.abknative.outgo.outgoing.network.SyncPushRequest

interface SyncNetworkApi {
    /**
     * Appelle la route POST /sync/push du serveur.
     */
    suspend fun pushData(request: SyncPushRequest): Result<Unit, AppException>

    /**
     * Appelle la route GET /sync/pull?since={timestamp} du serveur.
     */
    suspend fun pullData(since: Long): Result<SyncPullResponse, AppException>
}