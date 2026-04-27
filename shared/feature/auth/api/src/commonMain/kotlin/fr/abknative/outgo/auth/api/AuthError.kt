package fr.abknative.outgo.auth.api

import fr.abknative.outgo.core.api.logs.AppException

sealed class AuthError(cause: Throwable? = null) : AppException(cause) {
    class InvalidCredentials : AuthError()
    class EmailNotVerified : AuthError()
    class UserNotFound : AuthError()
    class SessionExpired : AuthError()
    class NeedsReauthentication : AuthError()
    class DataConflict : AuthError()
    class SystemError : AuthError()
}