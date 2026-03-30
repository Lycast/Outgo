package fr.abknative.outgo.wallet.network

import fr.abknative.outgo.wallet.network.dto.OperationNetworkDto
import fr.abknative.outgo.wallet.network.dto.WalletNetworkDto
import kotlinx.serialization.Serializable

@Serializable
data class SyncPushRequest(
    val wallets: List<WalletNetworkDto>,
    val operations: List<OperationNetworkDto>
)

@Serializable
data class SyncPullResponse(
    val wallets: List<WalletNetworkDto>,
    val operations: List<OperationNetworkDto>,
    val serverTimestamp: Long
)