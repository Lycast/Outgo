package fr.abknative.outgo.shell.api

import fr.abknative.outgo.core.api.EpochMillis
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.sync.api.model.SyncState
import fr.abknative.outgo.wallet.api.model.operation.OperationType
import fr.abknative.outgo.wallet.api.model.operation.Recurrence

/**
 * Global application state held by the Shell.
 * Provides transverse data required by the top-level UI (Header, Navigation).
 */
data class ShellState(
    val activeWalletId: String? = null,
    val isOperationFormVisible: Boolean = false,

    val operationIdToEdit: String? = null,
    val initialName: String = "",
    val initialAmount: String = "",
    val initialType: OperationType = OperationType.EXPENSE,
    val initialRecurrence: Recurrence = Recurrence.UNIQUE,
    val initialStartDate: EpochMillis? = null,
    val initialEndDate: EpochMillis? = null,

    // --- Theme State ---
    /** Indicates whether the theme preference has been loaded from local storage. */
    val isThemeInitialized: Boolean = false,
    /** Indicates whether the dark mode is currently active. */
    val isDarkMode: Boolean = false,

    val isPremium: Boolean = false,
    val error: AppException? = null,
    val syncState: SyncState = SyncState.UNAUTHENTICATED
)