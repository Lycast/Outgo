package fr.abknative.outgo.server.core.repository

import fr.abknative.outgo.wallet.network.dto.WalletNetworkDto

interface WalletRepository {
    fun upsertFromDto(userId: String, dto: WalletNetworkDto)
    fun getWalletsSince(userId: String, since: Long): List<WalletNetworkDto>
}