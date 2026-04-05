package fr.abknative.outgo.settings.api

sealed interface SettingsIntent {
    object Logout : SettingsIntent

    data class DeleteAccount(
        val wipeLocal: Boolean,
        val wipeServer: Boolean,
        val revokeAuth: Boolean
    ) : SettingsIntent

    object PurgeLocalData : SettingsIntent
    object RefreshSync : SettingsIntent
    object DismissError : SettingsIntent
    object ResetSuccessFlag : SettingsIntent
}