package fr.abknative.outgo.wallet.api.model.dashboard

import fr.abknative.outgo.core.api.EpochMillis
import fr.abknative.outgo.wallet.api.model.Operation

/**
 * Représente une occurrence projetée dans le temps d'une opération.
 * Garde la référence stricte vers la règle d'origine pour l'édition.
 */
data class ProjectedOperation(
    val operation: Operation,
    val projectedDate: EpochMillis
)