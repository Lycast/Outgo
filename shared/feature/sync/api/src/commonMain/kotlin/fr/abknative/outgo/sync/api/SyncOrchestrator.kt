package fr.abknative.outgo.sync.api

import kotlinx.coroutines.flow.SharedFlow

/**
 * Orchestrates the background and foreground synchronization processes.
 * Acts as the global "Control Tower", combining local database states and network availability
 * to trigger sync operations efficiently without overloading the server.
 */
interface SyncOrchestrator {

    val syncEvents: SharedFlow<SyncEvent>

    fun start()

    fun triggerManualSync()

    /** User agreed to override local data; proceeding with cloud data synchronization. */
    fun resolveConflictDownloadCloud()

    /** User rejected: aborting connection and reverting to local data view. */
    fun resolveConflictCancelLogin()
}