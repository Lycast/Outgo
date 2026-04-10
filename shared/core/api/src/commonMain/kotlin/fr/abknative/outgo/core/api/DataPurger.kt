package fr.abknative.outgo.core.api

/**
 * Contract implemented by each feature to clean up its own data.
 */
interface DataPurger {

    /**
     * Purges data from the module.
     * @param userId If null, purges everything. If provided, purges only for that user.
     */
    suspend fun purgeData(userId: String? = null)
}