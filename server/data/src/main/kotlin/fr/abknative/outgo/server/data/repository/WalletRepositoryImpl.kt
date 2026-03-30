package fr.abknative.outgo.server.data.repository

import fr.abknative.outgo.server.core.repository.WalletRepository
import fr.abknative.outgo.server.data.mapper.toEpochMillis
import fr.abknative.outgo.server.data.mapper.toSqlOffsetDateTime
import fr.abknative.outgo.server.data.tables.WalletsTable
import fr.abknative.outgo.wallet.network.dto.WalletNetworkDto
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greater
import org.jetbrains.exposed.v1.core.less
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.upsert

class WalletRepositoryImpl : WalletRepository {

    override fun upsertFromDto(userId: String, dto: WalletNetworkDto) {
        WalletsTable.upsert(
            WalletsTable.id,
            where = { WalletsTable.updatedAt less dto.updatedAt.toSqlOffsetDateTime() }
        ) { row ->
            row[id] = dto.id
            row[this.userId] = userId
            row[name] = dto.name

            row[createdAt] = dto.createdAt.toSqlOffsetDateTime()
            row[updatedAt] = dto.updatedAt.toSqlOffsetDateTime()
            row[deletedAt] = dto.deletedAt?.toSqlOffsetDateTime()
        }
    }

    override fun getWalletsSince(userId: String, since: Long): List<WalletNetworkDto> {
        val sinceOffsetDateTime = since.toSqlOffsetDateTime()
        return WalletsTable.selectAll().where {
            (WalletsTable.userId eq userId) and (WalletsTable.serverUpdatedAt greater sinceOffsetDateTime)
        }.map { row ->
            WalletNetworkDto(
                id = row[WalletsTable.id],
                name = row[WalletsTable.name],
                createdAt = row[WalletsTable.createdAt].toEpochMillis(),
                updatedAt = row[WalletsTable.updatedAt].toEpochMillis(),
                deletedAt = row[WalletsTable.deletedAt]?.toEpochMillis()
            )
        }
    }
}