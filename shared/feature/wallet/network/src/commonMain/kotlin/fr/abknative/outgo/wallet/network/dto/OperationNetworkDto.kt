package fr.abknative.outgo.wallet.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OperationNetworkDto(
    @SerialName("id") val id: String,
    @SerialName("wallet_id") val walletId: String,
    @SerialName("name") val name: String,
    @SerialName("amount_in_cents") val amountInCents: Long,
    @SerialName("type") val type: String,
    @SerialName("recurrence") val recurrence: String,
    @SerialName("start_date") val startDate: Long,
    @SerialName("end_date") val endDate: Long?,
    @SerialName("created_at") val createdAt: Long,
    @SerialName("updated_at") val updatedAt: Long,
    @SerialName("deleted_at") val deletedAt: Long?
)