package fr.abknative.outgo.auth.api.provider

import kotlinx.coroutines.flow.Flow

/**
 * Provides the current user identifier to secure local data access and ensure offline continuity.
 * This provider acts as the source of truth for the active identity, seamlessly bridging
 * local offline IDs and remote authenticated IDs.
 */
interface SessionProvider {
    /**
     * Retrieves the currently active user ID synchronously.
     * If the user is authenticated, this returns the remote ID.
     * If the user is offline/unauthenticated, this returns a stable, locally generated ID.
     *
     * @return The active user ID (e.g., a Firebase UID or a "local_..." UUID).
     */
    fun getCurrentUserId(): String

    /**
     * Observes changes to the active user ID reactively.
     * Useful for orchestrators (like SyncManager) or UI components that need to react
     * when the user's identity transitions.
     *
     * @return A Flow emitting the current user ID whenever it changes.
     */
    fun observeUserId(): Flow<String>

    /**
     * Formally locks the user ID into persistent storage.
     * This must ONLY be called after a successful authentication AND a successful
     * data migration/sync strategy resolution. It prevents premature identity mutation
     * if the network fails during the login or migration process.
     *
     * @param userId The validated ID to persist as the new truth.
     */
    fun commitPersistentId(userId: String)
}