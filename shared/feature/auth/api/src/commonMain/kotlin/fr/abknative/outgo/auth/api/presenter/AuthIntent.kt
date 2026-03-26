package fr.abknative.outgo.auth.api.presenter

sealed interface AuthIntent {
    object Login : AuthIntent
    object Logout : AuthIntent
}