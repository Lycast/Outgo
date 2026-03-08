package fr.abknative.outgo.outgoing.api.presenter

import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.outgoing.api.model.Outgoing

data class OutgoingState(
    val isLoading: Boolean = false,

    // --- Données de la liste ---
    val outgoings: List<Outgoing> = emptyList(),

    // --- Calculs et Budget ---
    val monthlyTotalInCents: Long = 0L,
    val userBudgetInCents: Long = 150000L,
    val remainingToPayThisMonthInCents: Long = 45050L,

    // --- État Global ---
    val isCloudSyncActive: Boolean = false,
    val error: AppException? = null
)