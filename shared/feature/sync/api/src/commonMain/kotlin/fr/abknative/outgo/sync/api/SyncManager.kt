package fr.abknative.outgo.sync.api

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result

/**
 * Main interface for data synchronization.
 * This contract is intended to be called by WorkManager (Android) or Background Tasks (iOS).
 */
interface SyncManager {

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

    fun clearSyncState()
}