package fr.abknative.outgo.wallet.network.mapper

import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.wallet.api.model.Wallet
import fr.abknative.outgo.wallet.network.dto.BudgetNetworkDto

// Réseau -> Domaine (PULL)
fun BudgetNetworkDto.toDomain(): Wallet {
    return Wallet(
        id = this.id,
        monthlyIncomeInCents = this.monthlyIncomeInCents,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        syncStatus = SyncStatus.SYNCED
    )
}

// Domaine -> Réseau (PUSH)
fun Wallet.toNetworkDto(): BudgetNetworkDto {
    return BudgetNetworkDto(
        id = this.id,
        monthlyIncomeInCents = this.monthlyIncomeInCents,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}