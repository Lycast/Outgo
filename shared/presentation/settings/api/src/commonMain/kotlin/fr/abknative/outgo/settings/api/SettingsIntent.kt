package fr.abknative.outgo.settings.api

sealed interface SettingsIntent {

    data class DeleteAccount(
        val wipeServer: Boolean,
        val revokeAuth: Boolean
    ) : SettingsIntent

    data class Logout(val displayLocalData: Boolean) : SettingsIntent
    object PurgeLocalData : SettingsIntent
    object DismissError : SettingsIntent
    object ResetSuccessFlag : SettingsIntent
}