package fr.abknative.outgo.settings.api

sealed interface SettingsIntent {
    object Logout : SettingsIntent
    object DeleteAccount : SettingsIntent
    object PurgeLocalData : SettingsIntent
    object RefreshSync : SettingsIntent
    object DismissError : SettingsIntent
    object ResetSuccessFlag : SettingsIntent
}