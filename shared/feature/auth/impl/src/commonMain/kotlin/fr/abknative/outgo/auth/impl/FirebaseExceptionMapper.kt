package fr.abknative.outgo.auth.impl

import dev.gitlive.firebase.auth.FirebaseAuthException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidUserException
import dev.gitlive.firebase.auth.FirebaseAuthRecentLoginRequiredException
import fr.abknative.outgo.auth.api.AuthError
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.AppLogger
import fr.abknative.outgo.core.api.logs.CommonError

/**
 * Traduit spécifiquement les exceptions techniques levées par le SDK Firebase
 * en exceptions métier [AppException] / [AuthError].
 */
internal fun Exception.toAuthAppException(tag: String = "FirebaseAuth"): AppException {
    AppLogger.get()?.e(tag, "Firebase operation failed", this)

    return when (this) {
        is FirebaseAuthRecentLoginRequiredException -> AuthError.NeedsReauthentication()
        is FirebaseAuthInvalidCredentialsException -> AuthError.InvalidCredentials()
        is FirebaseAuthInvalidUserException -> AuthError.UserNotFound()
        is FirebaseAuthException -> CommonError.Unauthorized(this)

        is AppException -> this

        else -> CommonError.UnknownError(this)
    }
}