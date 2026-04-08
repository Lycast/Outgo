package fr.abknative.outgo.sync.api

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import kotlinx.coroutines.flow.StateFlow

/**
 * Main interface for data synchronization.
 * This contract is intended to be called by WorkManager (Android) or Background Tasks (iOS).
 */
interface SyncManager {

    /**
     * Reactive stream indicating whether a synchronization is currently in progress.
     */
    val isSyncing: StateFlow<Boolean>

    /**
     * Performs a full synchronization: sends local data (Push)
     * and then fetches updates from the server (Pull).
     */
    suspend fun syncAll(): Result<Unit, AppException>

    /**
     * Sends local modifications to the server only (Push).
     */
    suspend fun syncOut(): Result<Unit, AppException>

    /**
     * Fetches updates from the server only (Pull).
     */
    suspend fun syncIn(): Result<Unit, AppException>

    /**
     * Checks if the authenticated account already has data on the server.
     * Helps detect potential conflicts with local data during the sign-in process.
     */
    suspend fun hasRemoteData(): Result<Boolean, AppException>

    fun clearSyncState()
}