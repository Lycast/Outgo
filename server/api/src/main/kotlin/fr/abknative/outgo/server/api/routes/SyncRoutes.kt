package fr.abknative.outgo.server.api.routes

import fr.abknative.outgo.outgoing.network.SyncPushRequest
import fr.abknative.outgo.server.api.extensions.userId
import fr.abknative.outgo.server.core.usecase.GetSyncPullUseCase
import fr.abknative.outgo.server.core.usecase.ProcessSyncPushUseCase
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory

fun Route.syncRoutes() {
    val processSyncPush by inject<ProcessSyncPushUseCase>()
    val getSyncPull by inject<GetSyncPullUseCase>()
    val logger = LoggerFactory.getLogger("SyncRouting")

    authenticate("auth-firebase") {
        route("/sync") {
            post("/push") {
                val userId = call.userId()
                val request = call.receive<SyncPushRequest>()

                logger.info("PUSH: Received ${request.budgets.size} budgets for $userId")
                processSyncPush(userId = userId, request = request)

                call.respond(HttpStatusCode.OK, "Sync Successful")
            }

            get("/pull") {
                val userId = call.userId()
                val since = call.request.queryParameters["since"]?.toLongOrNull() ?: 0L

                logger.info("PULL: $userId requesting updates since $since")
                val response = getSyncPull(userId = userId, since = since)

                call.respond(HttpStatusCode.OK, response)
            }
        }
    }
}