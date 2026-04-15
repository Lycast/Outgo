package fr.abknative.outgo.shell.api

import fr.abknative.outgo.core.api.EpochMillis
import fr.abknative.outgo.wallet.api.model.operation.OperationType
import fr.abknative.outgo.wallet.api.model.operation.Recurrence

sealed interface ShellIntent {

    /** Requests opening the global operation form. If id is null, it acts as a creation. */
    data class OpenOperationForm(
        val operationId: String? = null,
        val name: String = "",
        val amount: String = "",
        val type: OperationType = OperationType.EXPENSE,
        val recurrence: Recurrence = Recurrence.UNIQUE,
        val startDate: EpochMillis? = null,
        val endDate: EpochMillis? = null
    ) : ShellIntent

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