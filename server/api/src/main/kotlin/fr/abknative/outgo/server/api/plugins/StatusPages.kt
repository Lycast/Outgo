package fr.abknative.outgo.server.api.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import org.slf4j.LoggerFactory

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            val logger = LoggerFactory.getLogger("ExceptionHandler")
            logger.error("Unhandled exception", cause)
            call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
        }
    }
}