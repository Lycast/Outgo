package fr.abknative.outgo.outgoing.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BudgetNetworkDto(
    @SerialName("id") val id: String,
    @SerialName("monthly_income_cents") val monthlyIncomeInCents: Long
)