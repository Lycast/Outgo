package fr.abknative.outgo.outgoing.api.presenter

import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.outgoing.api.model.Outgoing

data class OutgoingState(
    val isLoading: Boolean = false,

    val outgoings: List<Outgoing> = emptyList(),

    val monthlyTotalInCents: Long = 0L,
    val remainingToPayThisMonthInCents: Long = 0L,

    val error: AppException? = null
)