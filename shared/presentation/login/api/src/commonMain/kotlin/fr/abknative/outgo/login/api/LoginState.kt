package fr.abknative.outgo.login.api

import fr.abknative.outgo.auth.api.model.UserSession
import fr.abknative.outgo.core.api.logs.AppException

data class LoginState(
    val session: UserSession? = null,
    val isLoading: Boolean = false,
    val error: AppException? = null,
    val showConflictDialog: Boolean = false
)