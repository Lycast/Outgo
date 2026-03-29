package fr.abknative.outgo.server.core.repository

import fr.abknative.outgo.wallet.network.dto.BudgetNetworkDto

interface BudgetRepository {
    fun upsertFromDto(userId: String, dto: BudgetNetworkDto)
    fun getBudgetsSince(userId: String, since: Long): List<BudgetNetworkDto>
}