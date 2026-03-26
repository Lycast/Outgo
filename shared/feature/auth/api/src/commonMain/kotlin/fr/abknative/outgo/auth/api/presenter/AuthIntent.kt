package fr.abknative.outgo.auth.api.presenter

sealed interface AuthIntent {
    data class SubmitLogin(val email: String, val password: String) : AuthIntent
    object LoginWithGoogle : AuthIntent
    object LoginWithApple : AuthIntent
    object Logout : AuthIntent
    object DismissError : AuthIntent
}