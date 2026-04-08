package fr.abknative.outgo.shell.api

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.sync.api.model.SyncState

/**
 * Global application state held by the Shell.
 * Provides transverse data required by the top-level UI (Header, Navigation).
 */
data class ShellState(
    val syncState: SyncState = SyncState.UNAUTHENTICATED,
    val isPremium: Boolean = false,
    val error: AppException? = null
)