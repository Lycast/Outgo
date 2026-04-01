package fr.abknative.outgo.server.api.routes

import fr.abknative.outgo.server.api.extensions.userId
import fr.abknative.outgo.server.core.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.userRoutes() {
    val userRepository by inject<UserRepository>()

    authenticate("auth-firebase") {
        route("/user") {
            delete("/me") {
                val userId = call.userId()

                userRepository.deleteUser(userId)

                call.respond(HttpStatusCode.OK, "User and associated data deleted successfully")
            }
        }
    }
}