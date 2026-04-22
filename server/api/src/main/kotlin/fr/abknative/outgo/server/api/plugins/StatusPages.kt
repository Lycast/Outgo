package fr.abknative.outgo.server.api.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.sentry.Sentry
import org.slf4j.LoggerFactory

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            val logger = LoggerFactory.getLogger("ExceptionHandler")
            logger.error("Unhandled exception", cause)
            Sentry.captureException(cause)
            call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
        }
        exception<RequestValidationException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, cause.reasons.joinToString())
        }
    }
}