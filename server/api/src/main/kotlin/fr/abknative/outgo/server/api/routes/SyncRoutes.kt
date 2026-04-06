package fr.abknative.outgo.server.api.routes

import fr.abknative.outgo.server.api.extensions.userEmail
import fr.abknative.outgo.server.api.extensions.userId
import fr.abknative.outgo.server.core.usecase.GetSyncPullUseCase
import fr.abknative.outgo.server.core.usecase.ProcessSyncPushUseCase
import fr.abknative.outgo.wallet.network.SyncPushRequest
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory

/**
 * Defines synchronization routes for pushing and pulling data.
 * All routes are protected by Firebase Authentication.
 */
fun Route.syncRoutes() {
    val processSyncPush by inject<ProcessSyncPushUseCase>()
    val getSyncPull by inject<GetSyncPullUseCase>()
    val logger = LoggerFactory.getLogger("SyncRouting")

    authenticate("auth-firebase") {
        route("/sync") {

            post("/push") {
                val userId = call.userId()
                val email = call.userEmail()
                val request = call.receive<SyncPushRequest>()

                logger.info("PUSH: Received ${request.wallets.size} wallets for user : $userId")

                processSyncPush(userId = userId, email = email, request = request)

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