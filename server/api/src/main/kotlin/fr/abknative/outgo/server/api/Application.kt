package fr.abknative.outgo.server.api

import fr.abknative.outgo.server.api.plugins.*
import fr.abknative.outgo.server.api.routes.adminRoutes
import fr.abknative.outgo.server.api.routes.syncRoutes
import fr.abknative.outgo.server.api.routes.userRoutes
import fr.abknative.outgo.server.data.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*

fun main() {

    val envPort = System.getenv("PORT")?.toIntOrNull() ?: 8080

    embeddedServer(Netty, port = envPort, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

fun Application.module() {

    DatabaseFactory.init()
    FirebaseAdmin.init()

    configureDependencyInjection()
    configureSerialization()
    configureSecurity()
    configureStatusPages()
    configureLogging()
    configureValidation()
    configureRateLimit()

    routing {
        syncRoutes()
        userRoutes()
        adminRoutes()
    }
}