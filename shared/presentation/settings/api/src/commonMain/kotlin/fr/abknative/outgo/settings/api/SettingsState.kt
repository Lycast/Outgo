package fr.abknative.outgo.settings.api

import fr.abknative.outgo.auth.api.model.UserSession
import fr.abknative.outgo.core.api.logs.AppException

data class SettingsState(
    val session: UserSession? = null,
    val isProcessing: Boolean = false,
    val error: AppException? = null,
    val actionSuccess: Boolean = false,
    val requireAccountDeletionLogin: Boolean = false
)