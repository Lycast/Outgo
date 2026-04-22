package fr.abknative.outgo.shell.api

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.shell.api.payload.OperationPayload
import fr.abknative.outgo.sync.api.model.SyncState

/**
 * Global application state held by the Shell.
 * Provides transverse data required by the top-level UI (Header, Navigation).
 */
data class ShellState(
    val activeWalletId: String? = null,
    val operationPayload: OperationPayload? = null,
    val todayFormatted: String = "",

    val isOperationFormVisible: Boolean = false,

    // --- Theme State ---
    /** Indicates whether the theme preference has been loaded from local storage. */
    val isThemeInitialized: Boolean = false,
    /** Indicates whether the dark mode is currently active. */
    val isDarkMode: Boolean = false,

    val isPremium: Boolean = false,
    val error: AppException? = null,
    val globalErrorMessage: String? = null,
    val syncState: SyncState = SyncState.UNAUTHENTICATED
)