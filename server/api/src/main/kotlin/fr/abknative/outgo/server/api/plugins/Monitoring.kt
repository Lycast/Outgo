package fr.abknative.outgo.server.api.plugins

import io.ktor.server.application.*
import io.opentelemetry.instrumentation.ktor.v3_0.KtorServerTelemetry
import io.sentry.Sentry
import io.sentry.SentryOptions

fun Application.configureMonitoring() {
    val sentryDsn = System.getenv("SENTRY_DSN")

    if (!sentryDsn.isNullOrBlank()) {
        Sentry.init { options: SentryOptions ->
            options.dsn = sentryDsn
            options.tracesSampleRate = 1.0

            // todo Correction ici :
            // Essaye 'enableOpenTelemetry' ou 'setEnableOpenTelemetry(true)'
        }

        // Le plugin Ktor 3 pour OpenTelemetry
        install(KtorServerTelemetry)
    }
}