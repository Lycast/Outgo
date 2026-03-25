package fr.abknative.outgo.server.data.repository

import fr.abknative.outgo.outgoing.network.dto.OutgoingNetworkDto
import fr.abknative.outgo.server.core.repository.OutgoingRepository
import fr.abknative.outgo.server.data.mapper.toEpochMillis
import fr.abknative.outgo.server.data.mapper.toSqlOffsetDateTime
import fr.abknative.outgo.server.data.tables.OutgoingsTable
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greater
import org.jetbrains.exposed.v1.core.less
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.upsert

class OutgoingRepositoryImpl : OutgoingRepository {

    override fun upsertFromDto(userId: String, dto: OutgoingNetworkDto) {
        OutgoingsTable.upsert(
            OutgoingsTable.id,
            where = { OutgoingsTable.updatedAt less dto.updatedAt.toSqlOffsetDateTime() }
        ) { row ->
            row[id] = dto.id
            row[this.userId] = userId
            row[budgetId] = dto.budgetId
            row[name] = dto.name
            row[amountInCents] = dto.amountInCents
            row[recurrence] = dto.recurrence
            row[dueDay] = dto.dueDay
            row[dueMonth] = dto.dueMonth

            row[createdAt] = dto.createdAt.toSqlOffsetDateTime()
            row[updatedAt] = dto.updatedAt.toSqlOffsetDateTime()

            row[isDeleted] = dto.isDeleted

        }
    }

    override fun getOutgoingsSince(userId: String, since: Long): List<OutgoingNetworkDto> {
        val sinceOffsetDateTime = since.toSqlOffsetDateTime()
        return OutgoingsTable.selectAll().where {
            (OutgoingsTable.userId eq userId) and (OutgoingsTable.serverUpdatedAt greater sinceOffsetDateTime)
        }.map { row ->
            OutgoingNetworkDto(
                id = row[OutgoingsTable.id],
                budgetId = row[OutgoingsTable.budgetId],
                name = row[OutgoingsTable.name],
                amountInCents = row[OutgoingsTable.amountInCents],
                recurrence = row[OutgoingsTable.recurrence],
                dueDay = row[OutgoingsTable.dueDay],
                dueMonth = row[OutgoingsTable.dueMonth],

                createdAt = row[OutgoingsTable.createdAt].toEpochMillis(),
                updatedAt = row[OutgoingsTable.updatedAt].toEpochMillis(),

                isDeleted = row[OutgoingsTable.isDeleted]
            )
        }
    }
}