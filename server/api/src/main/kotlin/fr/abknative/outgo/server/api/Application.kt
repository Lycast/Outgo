package fr.abknative.outgo.server.api

import fr.abknative.outgo.server.data.DatabaseFactory
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    DatabaseFactory.init()
    configureSecurity()
    install(ContentNegotiation) { json() }

    routing {
        authenticate("auth-firebase") {

            route("/sync") {

                post("/push") {
                    val userId = call.principal<UserIdPrincipal>()?.name
                    println("Log : Reception de données pour l'utilisateur $userId")
                    call.respond(HttpStatusCode.OK, "Données reçues et en attente de traitement")
                }

                get("/pull") {
                    val userId = call.principal<UserIdPrincipal>()?.name
                    val lastSync = call.request.queryParameters["since"] ?: "0"
                    println("Log : $userId demande les nouveautés depuis le timestamp : $lastSync")
                    call.respond(HttpStatusCode.OK, "Voici les nouveautés depuis $lastSync")
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