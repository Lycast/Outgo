package fr.abknative.outgo.core.api

/**
 * Contrat implémenté par chaque feature pour nettoyer ses propres données.
 */
interface DataPurger {
    suspend fun purgeData()
}