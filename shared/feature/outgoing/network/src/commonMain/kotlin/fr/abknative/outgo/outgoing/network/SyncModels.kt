package fr.abknative.outgo.outgoing.network

import fr.abknative.outgo.outgoing.network.dto.BudgetNetworkDto
import fr.abknative.outgo.outgoing.network.dto.OutgoingNetworkDto
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