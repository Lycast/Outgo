package fr.abknative.outgo.auth.api.presenter

import fr.abknative.outgo.auth.api.model.UserSession
import fr.abknative.outgo.core.api.logs.AppException

data class AuthState(
    val session: UserSession? = null,
    val isLoading: Boolean = false,
    val error: AppException? = null
)