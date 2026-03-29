package fr.abknative.outgo.wallet.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BudgetNetworkDto(
    @SerialName("id") val id: String,
    @SerialName("monthly_income_cents") val monthlyIncomeInCents: Long,
    @SerialName("created_at") val createdAt: Long,
    @SerialName("updated_at") val updatedAt: Long
)