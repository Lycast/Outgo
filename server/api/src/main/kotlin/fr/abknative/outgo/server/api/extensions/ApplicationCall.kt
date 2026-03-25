package fr.abknative.outgo.server.api.extensions

import io.ktor.server.application.*
import io.ktor.server.auth.*

fun ApplicationCall.userId(): String {
    return principal<UserIdPrincipal>()?.name
        ?: throw IllegalStateException("User principal not found")
}