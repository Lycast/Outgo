package fr.abknative.outgo.login.api

sealed interface LoginEvent {
    /** Signal sending the user back after a successful action. */
    data object NavigateBack : LoginEvent
}