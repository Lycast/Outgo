package fr.abknative.outgo.auth.api

import fr.abknative.outgo.core.api.logs.AppException

sealed class AuthError(cause: Throwable? = null) : AppException(cause) {
    class InvalidCredentials : AuthError()
    class UserNotFound : AuthError()
    class SessionExpired : AuthError()
}