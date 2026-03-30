package fr.abknative.outgo.server.core.repository

import fr.abknative.outgo.wallet.network.dto.OperationNetworkDto

interface OperationRepository {
    fun upsertFromDto(userId: String, dto: OperationNetworkDto)
    fun getOperationsSince(userId: String, since: Long): List<OperationNetworkDto>
}