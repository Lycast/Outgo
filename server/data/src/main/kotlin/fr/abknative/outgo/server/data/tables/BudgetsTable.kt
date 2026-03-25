package fr.abknative.outgo.server.data.tables

import org.jetbrains.exposed.v1.core.Expression
import org.jetbrains.exposed.v1.core.QueryBuilder
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone
import java.time.OffsetDateTime

object BudgetsTable : Table("budgets") {
    val id = varchar("id", 36)
    val userId = varchar("user_id", 128).references(UsersTable.id)
    val monthlyIncomeInCents = long("monthly_income_in_cents").default(0L)
    val createdAt = timestampWithTimeZone("created_at")
    val updatedAt = timestampWithTimeZone("updated_at")
    val serverUpdatedAt = timestampWithTimeZone("server_updated_at")
        .defaultExpression(object : Expression<OffsetDateTime>() {
            override fun toQueryBuilder(queryBuilder: QueryBuilder) {
                queryBuilder.append("CURRENT_TIMESTAMP")
            }
        })

    override val primaryKey = PrimaryKey(id)
}