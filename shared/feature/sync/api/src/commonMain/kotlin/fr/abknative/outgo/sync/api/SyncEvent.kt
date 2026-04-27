package fr.abknative.outgo.sync.api

import fr.abknative.outgo.core.api.logs.AppException

/**
 * Represents global events emitted by the synchronization engine
 * that require immediate attention from the top-level UI (Shell).
 */
sealed interface SyncEvent {

    /**
     * Emitted when the orchestrator starts verifying if the newly authenticated user
     * already possesses remote data on the server.
     * The UI should intercept this event to display a loading state, preventing
     * premature navigation or user interactions during network latency (e.g., server cold starts).
     */
    data object CheckRemoteDataStarted : SyncEvent

    /**
     * Emitted when the initial remote data verification and any subsequent local data
     * migrations are fully completed.
     * The UI should intercept this event to dismiss the loading state and resume the normal flow.
     */
    data object CheckRemoteDataFinished : SyncEvent

    /**
     * Emitted when a user logs into an existing cloud account
     * while still having unsynchronized local data.
     * The UI (Shell) MUST intercept this event and prompt the user
     * to choose a resolution strategy (e.g., Keep Cloud vs Overwrite with Local).
     */
    data object ConflictRequiresResolution : SyncEvent

    /**
     * Emitted when a background synchronization fails critically
     * (e.g., network timeout, server error).
     * The UI should typically display this in a global Snackbar.
     */
    data class Error(val exception: AppException) : SyncEvent
}