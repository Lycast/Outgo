package fr.abknative.outgo.sync.api

import fr.abknative.outgo.core.api.logs.AppException

/**
 * Represents global events emitted by the synchronization engine
 * that require immediate attention from the top-level UI (Shell).
 */
sealed interface SyncEvent {

    /**
     * Emitted when a background synchronization fails critically
     * (e.g., network timeout, server error).
     * The UI should typically display this in a global Snackbar.
     */
    data class Error(val exception: AppException) : SyncEvent

    /**
     * Emitted when a user logs into an existing cloud account
     * while still having unsynchronized local data.
     * The UI (Shell) MUST intercept this event and prompt the user
     * to choose a resolution strategy (e.g., Keep Cloud vs Overwrite with Local).
     */
    data object ConflictRequiresResolution : SyncEvent
}