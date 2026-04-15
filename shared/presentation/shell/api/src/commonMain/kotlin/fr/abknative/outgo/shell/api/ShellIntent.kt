package fr.abknative.outgo.shell.api

import fr.abknative.outgo.shell.api.payload.OperationPayload

sealed interface ShellIntent {

    /** * Requests opening the global operation form.
     * Pass an [OperationPayload] with data for edition, or an empty one for creation.
     */
    data class OpenOperationForm(val payload: OperationPayload) : ShellIntent

    /** Closes the global operation form. */
    data object CloseOperationForm : ShellIntent

    /** Intent to manually trigger a cloud synchronization from the global header. */
    object RefreshSync : ShellIntent

    /** Intent to clear any global error message. */
    object DismissError : ShellIntent

    // --- Theme Intents ---
    /**
     * Initializes the application theme.
     * Uses the provided system default if no user preference is found in storage.
     */
    data class InitTheme(val systemDefaultIsDark: Boolean) : ShellIntent

    /**
     * Updates the dark mode preference and saves it persistently.
     */
    data class UpdateDarkMode(val isDarkMode: Boolean) : ShellIntent
}