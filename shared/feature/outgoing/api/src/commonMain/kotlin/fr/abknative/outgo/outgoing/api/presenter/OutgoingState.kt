package fr.abknative.outgo.outgoing.api.presenter

import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.outgoing.api.model.Outgoing

data class OutgoingState(
    val isLoading: Boolean = false,

    // --- Données de la liste ---
    val outgoings: List<Outgoing> = emptyList(),
    val todayLabel: String = "",

    // --- Calculs et Budget ---
    val monthlyIncomeInCents: Long = 0L,        // salaire / budget global
    val totalOutgoingsInCents: Long = 0L,       // Somme lissée des charges
    val disposableIncomeInCents: Long = 0L,     // ce qu'il te reste du budget
    val remainingToPayInCents: Long = 0L,       // Ce qui va encore sortir ce mois-ci

    // --- État Global ---
    val isCloudSyncActive: Boolean = false,
    val error: AppException? = null
)