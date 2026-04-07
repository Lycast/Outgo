package fr.abknative.outgo.server.api

import fr.abknative.outgo.server.api.plugins.*
import fr.abknative.outgo.server.api.routes.adminRoutes
import fr.abknative.outgo.server.api.routes.syncRoutes
import fr.abknative.outgo.server.api.routes.userRoutes
import fr.abknative.outgo.server.data.DatabaseFactory
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Main entry point for the Outgo API server.
 * Initializes the embedded Netty server with the specified port.
 */
fun main() {
    val envPort = System.getenv("PORT")?.toIntOrNull() ?: 8080

    embeddedServer(Netty, port = envPort, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

/**
 * Ktor Application module configuration.
 * Sets up logging, database, Firebase, dependency injection, and routing.
 * Wrapped in a global try/catch to expose startup errors on the /health endpoint.
 */
fun Application.module() {

    configureLogging()
    configureSerialization()
    configureStatusPages()

    var startupStatus = "API Outgo 100% Opérationnelle !"

    try {
        // On tente d'initialiser tous les modules critiques
        DatabaseFactory.init()
        FirebaseAdmin.init()

        configureDependencyInjection()
        configureSecurity()
        configureValidation()
        configureRateLimit()

        // On active les vraies routes uniquement si tout a réussi
        routing {
            syncRoutes()
            userRoutes()
            adminRoutes()
        }
    } catch (e: Exception) {
        // En cas de crash, on capture l'erreur exacte et on la stocke
        startupStatus = "CRASH D'INITIALISATION : ${e.message} \n Cause : ${e.cause}"
        println(startupStatus) // Utile pour les logs internes
    }

    // Le point de survie : cette route est chargée quoi qu'il arrive
    routing {
        get("/health") {
            call.respondText(startupStatus)
        }
    }
}