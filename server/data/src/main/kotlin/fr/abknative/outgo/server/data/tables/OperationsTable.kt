package fr.abknative.outgo.server.data.tables

import org.jetbrains.exposed.v1.core.Expression
import org.jetbrains.exposed.v1.core.QueryBuilder
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone
import java.time.OffsetDateTime

object OperationsTable : Table("operations") {
    val id = varchar("id", 36)
    val walletId = varchar("wallet_id", 36).references(WalletsTable.id)
    val userId = varchar("user_id", 128).references(UsersTable.id)
    val name = varchar("name", 255)
    val amountInCents = long("amount_in_cents")
    val type = varchar("type", 20) // INCOME / EXPENSE
    val recurrence = varchar("recurrence", 50)

    // Moteur temporel métier (Stocké en BIGINT/Long comme sur le mobile)
    val startDate = long("start_date")
    val endDate = long("end_date").nullable()

    // Timestamps synchronisation
    val createdAt = timestampWithTimeZone("created_at")
    val updatedAt = timestampWithTimeZone("updated_at")
    val deletedAt = timestampWithTimeZone("deleted_at").nullable()

    val serverUpdatedAt = timestampWithTimeZone("server_updated_at")
        .defaultExpression(object : Expression<OffsetDateTime>() {
            override fun toQueryBuilder(queryBuilder: QueryBuilder) {
                queryBuilder.append("CURRENT_TIMESTAMP")
            }
        })

    override val primaryKey = PrimaryKey(id)
}