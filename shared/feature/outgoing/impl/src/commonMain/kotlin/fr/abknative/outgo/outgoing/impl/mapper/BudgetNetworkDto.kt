package fr.abknative.outgo.outgoing.impl.mapper

import fr.abknative.outgo.outgoing.api.model.Budget
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class BudgetNetworkDto (
    @SerialName("id") val id: String,
    @SerialName("monthly_income_cents") val monthlyIncomeInCents: Long
)

internal fun BudgetNetworkDto.toDomain(): Budget {
    return Budget(
        id = this.id,
        monthlyIncomeInCents = this.monthlyIncomeInCents
    )
}