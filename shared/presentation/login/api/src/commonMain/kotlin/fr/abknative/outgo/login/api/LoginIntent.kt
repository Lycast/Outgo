package fr.abknative.outgo.login.api

sealed interface LoginIntent {
    data class SubmitLogin(val email: String, val password: String) : LoginIntent
    object LoginWithGoogle : LoginIntent
    object LoginWithApple : LoginIntent
    object Logout : LoginIntent
    object DismissError : LoginIntent
}