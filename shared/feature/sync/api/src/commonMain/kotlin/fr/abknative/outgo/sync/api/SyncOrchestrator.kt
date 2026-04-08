package fr.abknative.outgo.sync.api

/**
 * Orchestrates the background and foreground synchronization processes.
 * Acts as the global "Control Tower", combining local database states and network availability
 * to trigger sync operations efficiently without overloading the server.
 */
interface SyncOrchestrator {

    /**
     * Initializes the orchestrator's observation loops.
     * Should be called exactly once during the application's lifecycle (e.g., at startup).
     * * This method performs two primary tasks:
     * 1. Checks if a full remote sync (Pull) is required due to a prolonged offline period.
     * 2. Starts observing local databases for pending changes to trigger a debounced push.
     */
    fun start()
}