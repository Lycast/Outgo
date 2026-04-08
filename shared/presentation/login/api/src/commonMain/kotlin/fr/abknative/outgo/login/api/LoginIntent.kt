package fr.abknative.outgo.login.api

import fr.abknative.outgo.auth.api.model.ConflictStrategy

sealed interface LoginIntent {
    data class SubmitRegister(val email: String, val password: String) : LoginIntent
    data class SubmitLogin(val email: String, val password: String) : LoginIntent
    object LoginWithGoogle : LoginIntent
    object LoginWithApple : LoginIntent
    object Logout : LoginIntent
    object DismissError : LoginIntent

    data class ResolveConflict(val strategy: ConflictStrategy) : LoginIntent
    object CancelConflict : LoginIntent
}