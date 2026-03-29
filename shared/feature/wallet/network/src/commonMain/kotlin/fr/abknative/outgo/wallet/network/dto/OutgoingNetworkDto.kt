package fr.abknative.outgo.wallet.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OutgoingNetworkDto(
    @SerialName("id") val id: String,
    @SerialName("budget_id") val budgetId: String,
    @SerialName("name") val name: String,
    @SerialName("amount_cents") val amountInCents: Long,
    @SerialName("recurrence") val recurrence: String,
    @SerialName("due_day") val dueDay: Int,
    @SerialName("due_month") val dueMonth: Int? = null,
    @SerialName("created_at") val createdAt: Long,
    @SerialName("updated_at") val updatedAt: Long,
    @SerialName("is_deleted") val isDeleted: Boolean = false
)