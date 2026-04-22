package fr.abknative.outgo.server.api.plugins

import io.ktor.server.application.*
import io.sentry.Sentry

fun Application.configureMonitoring() {
    val sentryDsn = System.getenv("SENTRY_DSN")

    if (!sentryDsn.isNullOrBlank()) {
        Sentry.init { options ->
            options.dsn = sentryDsn
            options.tracesSampleRate = 1.0
        }
    }
}