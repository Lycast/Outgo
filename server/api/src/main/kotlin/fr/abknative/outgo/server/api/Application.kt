package fr.abknative.outgo.server.api

import fr.abknative.outgo.server.api.plugins.configureDependencyInjection
import fr.abknative.outgo.server.api.plugins.configureSecurity
import fr.abknative.outgo.server.api.plugins.configureSerialization
import fr.abknative.outgo.server.api.plugins.configureStatusPages
import fr.abknative.outgo.server.api.routes.syncRoutes
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
    // 1. Initialisation des données
    DatabaseFactory.init()

    configureDependencyInjection() // Contient install(Koin)
    configureSerialization()       // Contient install(ContentNegotiation)
    configureSecurity()            // Contient install(Authentication)
    configureStatusPages()         // Gestion globale des erreurs

    // 3. Routage
    routing {
        syncRoutes()
    }
}