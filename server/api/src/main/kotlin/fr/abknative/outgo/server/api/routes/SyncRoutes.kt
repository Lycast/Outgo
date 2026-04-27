package fr.abknative.outgo.server.api.routes

import fr.abknative.outgo.server.api.extensions.isEmailVerified
import fr.abknative.outgo.server.api.extensions.userEmail
import fr.abknative.outgo.server.api.extensions.userId
import fr.abknative.outgo.server.core.usecase.GetSyncPullUseCase
import fr.abknative.outgo.server.core.usecase.ProcessSyncPushUseCase
import fr.abknative.outgo.wallet.network.SyncPushRequest
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.ratelimit.*
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
        rateLimit(RateLimitName("sync-limit")) {
            route("/sync") {
                post("/push") {

                    if (!call.isEmailVerified()) {
                        logger.warn("PUSH BLOCKED: User ${call.userId()} attempted to push with unverified email.")
                        call.respond(HttpStatusCode.Forbidden, "Email verification required to push data.")
                        return@post
                    }

                    val userId = call.userId()
                    val email = call.userEmail()
                    val request = call.receive<SyncPushRequest>()

                    if (request.wallets.isEmpty() && request.operations.isEmpty()) {
                        call.respond(HttpStatusCode.NoContent)
                        return@post
                    }

                    logger.info("PUSH: Received ${request.wallets.size} wallets / ${request.operations.size} ops for user: $userId")
                    processSyncPush(userId = userId, email = email, request = request)

                    call.respond(HttpStatusCode.OK, "Sync Successful")
                }

                get("/pull") {
                    call.response.header(HttpHeaders.CacheControl, "no-store, no-cache, must-revalidate")
                    val userId = call.userId()
                    val since = call.request.queryParameters["since"]?.toLongOrNull() ?: 0L

                    logger.info("PULL: $userId requesting updates since $since")
                    val response = getSyncPull(userId = userId, since = since)

                    call.respond(HttpStatusCode.OK, response)
                }
            }
        }
    }
}