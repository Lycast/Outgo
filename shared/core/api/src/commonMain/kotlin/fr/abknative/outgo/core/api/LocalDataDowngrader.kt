package fr.abknative.outgo.core.api

/**
 * Contract implemented by domain modules to gracefully downgrade their local
 * synchronization state when the remote server data is wiped.
 * This ensures the offline-first architecture can re-upload existing data
 * to a fresh server instance.
 */
interface LocalDataDowngrader {
    suspend fun downgradeAllToPendingCreate()
}