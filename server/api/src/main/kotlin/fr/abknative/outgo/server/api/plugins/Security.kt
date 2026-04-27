package fr.abknative.outgo.server.api.plugins

import fr.abknative.outgo.server.api.FirebaseAdmin
import io.ktor.server.application.*
import io.ktor.server.auth.*

data class UserPrincipal(val uid: String, val email: String?, val isEmailVerified: Boolean)

fun Application.configureSecurity() {
    install(Authentication) {
        bearer("auth-firebase") {
            authenticate { tokenCredential ->
                val token = tokenCredential.token
                val firebaseToken = FirebaseAdmin.verifyToken(token)
                if (firebaseToken != null) {
                    UserPrincipal(
                        uid = firebaseToken.uid,
                        email = firebaseToken.email,
                        isEmailVerified = firebaseToken.isEmailVerified
                    )
                } else { null }
            }
        }
    }
}