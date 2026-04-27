package fr.abknative.outgo.auth.api.model

data class UserSession(
    val userId: String,
    val token: String,
    val email: String,
    val isEmailVerified: Boolean
)