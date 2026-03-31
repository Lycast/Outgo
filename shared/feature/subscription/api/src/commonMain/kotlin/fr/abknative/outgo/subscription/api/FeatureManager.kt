package fr.abknative.outgo.subscription.api

import kotlinx.coroutines.flow.StateFlow

interface FeatureManager {
    /**
     * Retourne un flux réactif indiquant si l'utilisateur est actuellement Premium.
     * Utile pour l'UI qui doit masquer/afficher des boutons en temps réel.
     */
    val isPremiumFlow: StateFlow<Boolean>

    /**
     * Vérification "one-shot" synchrone.
     */
    fun isPremium(): Boolean

    /**
     * Met à jour la date de fin de l'abonnement en cache (appelé après une synchro réussie).
     */
    fun updatePremiumStatus(untilTimestamp: Long)
}