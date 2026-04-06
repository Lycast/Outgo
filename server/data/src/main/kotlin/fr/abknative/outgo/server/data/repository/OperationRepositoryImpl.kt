package fr.abknative.outgo.server.data.repository

import fr.abknative.outgo.server.core.repository.OperationRepository
import fr.abknative.outgo.server.data.mapper.toEpochMillis
import fr.abknative.outgo.server.data.mapper.toSqlOffsetDateTime
import fr.abknative.outgo.server.data.tables.OperationsTable
import fr.abknative.outgo.wallet.network.dto.OperationNetworkDto
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greater
import org.jetbrains.exposed.v1.core.less
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.upsert
import java.time.OffsetDateTime
import java.time.ZoneOffset

class OperationRepositoryImpl : OperationRepository {

    override fun upsertFromDto(userId: String, dto: OperationNetworkDto) {
        OperationsTable.upsert(
            OperationsTable.id,
            where = { OperationsTable.updatedAt less dto.updatedAt.toSqlOffsetDateTime() }
        ) { row ->
            row[id] = dto.id
            row[walletId] = dto.walletId
            row[this.userId] = userId
            row[name] = dto.name
            row[amountInCents] = dto.amountInCents
            row[type] = dto.type
            row[recurrence] = dto.recurrence

            // Moteur temporel
            row[startDate] = dto.startDate
            row[endDate] = dto.endDate

            // Timestamps de synchro
            row[createdAt] = dto.createdAt.toSqlOffsetDateTime()
            row[updatedAt] = dto.updatedAt.toSqlOffsetDateTime()
            row[deletedAt] = dto.deletedAt?.toSqlOffsetDateTime()

            row[serverUpdatedAt] = OffsetDateTime.now(ZoneOffset.UTC)
        }
    }

    override fun getOperationsSince(userId: String, since: Long): List<OperationNetworkDto> {
        val sinceOffsetDateTime = since.toSqlOffsetDateTime()
        return OperationsTable.selectAll().where {
            (OperationsTable.userId eq userId) and (OperationsTable.serverUpdatedAt greater sinceOffsetDateTime)
        }.map { row ->
            OperationNetworkDto(
                id = row[OperationsTable.id],
                walletId = row[OperationsTable.walletId],
                name = row[OperationsTable.name],
                amountInCents = row[OperationsTable.amountInCents],
                type = row[OperationsTable.type],
                recurrence = row[OperationsTable.recurrence],
                startDate = row[OperationsTable.startDate],
                endDate = row[OperationsTable.endDate],
                createdAt = row[OperationsTable.createdAt].toEpochMillis(),
                updatedAt = row[OperationsTable.updatedAt].toEpochMillis(),
                deletedAt = row[OperationsTable.deletedAt]?.toEpochMillis()
            )
        }
    }
}