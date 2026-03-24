package fr.abknative.outgo.server.api

import fr.abknative.outgo.outgoing.network.SyncPushRequest
import fr.abknative.outgo.server.api.di.serverModule
import fr.abknative.outgo.server.core.usecase.GetSyncPullUseCase
import fr.abknative.outgo.server.core.usecase.ProcessSyncPushUseCase
import fr.abknative.outgo.server.data.DatabaseFactory
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    DatabaseFactory.init()
    configureSecurity()
    install(ContentNegotiation) { json() }
    install(Koin) { modules(serverModule) }

    val processSyncPush by inject<ProcessSyncPushUseCase>()
    val getSyncPull by inject<GetSyncPullUseCase>()

    routing {
        authenticate("auth-firebase") {

            route("/sync") {

                post("/push") {
                    val userId = call.principal<UserIdPrincipal>()?.name
                    if (userId == null) {
                        call.respond(HttpStatusCode.Unauthorized, "Utilisateur non identifié")
                        return@post
                    }

                    try {
                        val request = call.receive<SyncPushRequest>()
                        println("Log : Réception de ${request.budgets.size} budgets et ${request.outgoings.size} dépenses pour $userId")

                        processSyncPush(userId = userId, request = request)

                        call.respond(HttpStatusCode.OK, "Synchronisation réussie")
                    } catch (e: Exception) {
                        println("Erreur lors de la synchro pour $userId: ${e.message}")
                        e.printStackTrace()
                        call.respond(HttpStatusCode.InternalServerError, "Erreur lors du traitement des données")
                    }
                }

                get("/pull") {
                    val userId = call.principal<UserIdPrincipal>()?.name
                    if (userId == null) {
                        call.respond(HttpStatusCode.Unauthorized, "Utilisateur non identifié")
                        return@get
                    }

                    try {
                        val sinceParam = call.request.queryParameters["since"]
                        val since = sinceParam?.toLongOrNull() ?: 0L

                        println("Log : $userId demande les nouveautés depuis le timestamp : $since")

                        val response = getSyncPull(userId = userId, since = since)

                        call.respond(HttpStatusCode.OK, response)

                    } catch (e: Exception) {
                        println("Erreur lors du pull pour $userId: ${e.message}")
                        e.printStackTrace()
                        call.respond(HttpStatusCode.InternalServerError, "Erreur lors de la récupération des données")
                    }
                }
            }
        }
    }
}

fun Application.configureSecurity() {
    install(Authentication) {
        bearer("auth-firebase") {
            authenticate { tokenCredential ->
                if (tokenCredential.token == "debug") {
                    UserIdPrincipal("user_debug_123")
                } else {
                    null
                }
            }
        }
    }
}