package fr.abknative.outgo.server.core.repository

interface GarbageCollectorRepository {
    /**
     * Purge physiquement les données marquées comme supprimées depuis plus de [daysOld] jours.
     * @return Un résumé : Pair(Nombre_Wallets_Supprimés, Nombre_Operations_Supprimées)
     */
    fun purgeOldDeletedData(daysOld: Long): Pair<Int, Int>
}