package fr.abknative.outgo.server.core.repository

import fr.abknative.outgo.wallet.network.dto.OutgoingNetworkDto

interface OutgoingRepository {
    fun upsertFromDto(userId: String, dto: OutgoingNetworkDto)
    fun getOutgoingsSince(userId: String, since: Long): List<OutgoingNetworkDto>
}