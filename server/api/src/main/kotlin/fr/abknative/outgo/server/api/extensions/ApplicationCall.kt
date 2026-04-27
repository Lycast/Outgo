package fr.abknative.outgo.server.api.extensions

import fr.abknative.outgo.server.api.plugins.UserPrincipal
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun ApplicationCall.userId(): String {
    return principal<UserPrincipal>()?.uid
        ?: throw IllegalStateException("User UID not found in principal")
}

fun ApplicationCall.userEmail(): String {
    return principal<UserPrincipal>()?.email ?: "unknown@outgo.app"
}

fun ApplicationCall.isEmailVerified(): Boolean {
    return principal<UserPrincipal>()?.isEmailVerified ?: false
}