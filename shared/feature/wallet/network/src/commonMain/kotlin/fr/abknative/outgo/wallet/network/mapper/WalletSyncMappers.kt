package fr.abknative.outgo.wallet.network.mapper

import fr.abknative.outgo.core.api.model.SyncStatus
import fr.abknative.outgo.wallet.api.model.Wallet
import fr.abknative.outgo.wallet.network.dto.WalletNetworkDto

// Réseau -> Domaine (PULL)
fun WalletNetworkDto.toDomain(): Wallet {
    return Wallet(
        id = this.id,
        name = name,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deletedAt = deletedAt,
        syncStatus = SyncStatus.SYNCED
    )
}

// Domaine -> Réseau (PUSH)
fun Wallet.toNetworkDto(): WalletNetworkDto {
    return WalletNetworkDto(
        id = this.id,
        name = name,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        deletedAt = deletedAt
    )
}