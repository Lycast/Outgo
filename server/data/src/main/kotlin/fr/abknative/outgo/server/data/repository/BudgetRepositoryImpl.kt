package fr.abknative.outgo.server.data.repository

import fr.abknative.outgo.outgoing.network.dto.BudgetNetworkDto
import fr.abknative.outgo.server.core.repository.BudgetRepository
import fr.abknative.outgo.server.data.mapper.toEpochMillis
import fr.abknative.outgo.server.data.mapper.toSqlOffsetDateTime
import fr.abknative.outgo.server.data.tables.BudgetsTable
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greater
import org.jetbrains.exposed.v1.core.less
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.upsert


class BudgetRepositoryImpl : BudgetRepository {

    override fun upsertFromDto(userId: String, dto: BudgetNetworkDto) {
        transaction {
            BudgetsTable.upsert(
                BudgetsTable.id,
                where = { BudgetsTable.updatedAt less dto.updatedAt.toSqlOffsetDateTime() }
            ) { row ->
                row[id] = dto.id
                row[this.userId] = userId
                row[monthlyIncomeInCents] = dto.monthlyIncomeInCents
                row[updatedAt] = dto.updatedAt.toSqlOffsetDateTime()

                row[createdAt] = dto.createdAt.toSqlOffsetDateTime()
            }
        }
    }

    override fun getBudgetsSince(userId: String, since: Long): List<BudgetNetworkDto> {
        return transaction {
            val sinceOffsetDateTime = since.toSqlOffsetDateTime()

            BudgetsTable.selectAll().where {
                (BudgetsTable.userId eq userId) and (BudgetsTable.serverUpdatedAt greater sinceOffsetDateTime)
            }.map { row ->
                BudgetNetworkDto(
                    id = row[BudgetsTable.id],
                    monthlyIncomeInCents = row[BudgetsTable.monthlyIncomeInCents],
                    createdAt = row[BudgetsTable.createdAt].toEpochMillis(),
                    updatedAt = row[BudgetsTable.updatedAt].toEpochMillis()
                )
            }
        }
    }
}