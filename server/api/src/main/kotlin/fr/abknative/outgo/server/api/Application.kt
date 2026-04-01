package fr.abknative.outgo.server.api

import fr.abknative.outgo.server.api.plugins.*
import fr.abknative.outgo.server.api.routes.syncRoutes
import fr.abknative.outgo.server.api.routes.userRoutes
import fr.abknative.outgo.server.data.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    DatabaseFactory.init()

    configureDependencyInjection()
    configureSerialization()
    configureSecurity()
    configureStatusPages()
    configureLogging()
    configureValidation()
    configureGarbageCollector()

    routing {
        syncRoutes()
        userRoutes()
    }
}