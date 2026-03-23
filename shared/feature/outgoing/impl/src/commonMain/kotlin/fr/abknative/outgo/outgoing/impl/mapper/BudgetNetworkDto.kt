package fr.abknative.outgo.outgoing.impl.mapper

import fr.abknative.outgo.outgoing.api.model.Budget
import fr.abknative.outgo.outgoing.network.BudgetNetworkDto

// Réseau -> Domaine (PULL)
internal fun BudgetNetworkDto.toDomain(): Budget {
    return Budget(
        id = this.id,
        monthlyIncomeInCents = this.monthlyIncomeInCents
    )
}

// Domaine -> Réseau (PUSH)
internal fun Budget.toNetworkDto(): BudgetNetworkDto {
    return BudgetNetworkDto(
        id = this.id,
        monthlyIncomeInCents = this.monthlyIncomeInCents
    )
}