package fr.abknative.outgo.auth.api.provider

/**
 * Provides the current user identifier to secure local data access.
 */
interface SessionProvider {
    /**
     * Retrieves the active `userId`.
     * If no authenticated user is present, returns a stable offline fallback ID.
     */
    fun getCurrentUserId(): String
}