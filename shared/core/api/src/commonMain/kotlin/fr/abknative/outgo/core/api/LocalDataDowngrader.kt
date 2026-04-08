package fr.abknative.outgo.core.api

/**
 * Contract implemented by domain modules to gracefully handle local data
 * when remote server data is wiped or a cloud account is deleted.
 */
interface LocalDataDowngrader {

    /**
     * Resets the synchronization status of all local data associated with the given [userId]
     * to a pending creation state.
     *
     * This is used when the remote server data is wiped, but the user remains authenticated.
     * It ensures the existing local data will be re-uploaded to the fresh server instance
     * during the next synchronization cycle.
     *
     * @param userId The identifier of the currently authenticated user.
     * @param now The current timestamp in milliseconds to update the modification date.
     */
    suspend fun resetSyncStatusToPending(userId: String, now: Long)

    /**
     * Transfers ownership of all local data from a Firebase UID to a new offline local ID,
     * and resets its synchronization status.
     *
     * This is used when the user deletes their cloud account. The data remains available
     * on the device but is detached from the cloud and returned to an offline-first anonymous state.
     *
     * @param firebaseId The Firebase UID of the account being deleted.
     * @param newLocalId The new local identifier to assign to the data.
     * @param now The current timestamp in milliseconds.
     */
    suspend fun downgradeToLocal(firebaseId: String, newLocalId: String, now: Long)
}