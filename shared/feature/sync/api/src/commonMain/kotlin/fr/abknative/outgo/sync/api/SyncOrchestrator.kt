package fr.abknative.outgo.sync.api

import fr.abknative.outgo.core.api.logs.AppException
import kotlinx.coroutines.flow.SharedFlow

/**
 * Orchestrates the background and foreground synchronization processes.
 * Acts as the global "Control Tower", combining local database states and network availability
 * to trigger sync operations efficiently without overloading the server.
 */
interface SyncOrchestrator {

    val syncEvents: SharedFlow<SyncEvent>

    fun start()
}

sealed interface SyncEvent {
    data class Error(val exception: AppException) : SyncEvent
}