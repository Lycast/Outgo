package fr.abknative.outgo.server.api.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.ratelimit.*
import kotlin.time.Duration.Companion.minutes

fun Application.configureRateLimit() {
    install(RateLimit) {
        global {
            rateLimiter(limit = 60, refillPeriod = 1.minutes)
            requestKey { call ->
                call.request.local.remoteHost
            }
        }
    }
}