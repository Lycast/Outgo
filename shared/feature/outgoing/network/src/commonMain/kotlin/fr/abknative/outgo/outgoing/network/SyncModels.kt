package fr.abknative.outgo.outgoing.network

import kotlinx.serialization.Serializable

@Serializable
data class SyncPushRequest(
    val outgoings: List<OutgoingNetworkDto>,
    val budgets: List<BudgetNetworkDto>
)

@Serializable
data class SyncPullResponse(
    val outgoings: List<OutgoingNetworkDto>,
    val budgets: List<BudgetNetworkDto>,
    val serverTimestamp: Long
)