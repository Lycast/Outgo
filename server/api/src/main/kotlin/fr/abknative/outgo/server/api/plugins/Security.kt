package fr.abknative.outgo.server.api.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureSecurity() {
    install(Authentication) {
        bearer("auth-firebase") {
            authenticate { tokenCredential ->
                // Ta logique de debug ou Firebase
                if (tokenCredential.token == "debug") {
                    UserIdPrincipal("user_debug_123")
                } else {
                    null
                }
            }
        }
    }
}