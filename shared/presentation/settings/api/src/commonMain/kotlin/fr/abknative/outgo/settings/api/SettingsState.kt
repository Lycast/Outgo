package fr.abknative.outgo.settings.api

import fr.abknative.outgo.auth.api.model.UserSession
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.sync.api.model.SyncState

data class SettingsState(
    val session: UserSession? = null,
    val syncState: SyncState = SyncState.UNAUTHENTICATED,
    val isProcessing: Boolean = false,
    val error: AppException? = null,
    val actionSuccess: Boolean = false
)