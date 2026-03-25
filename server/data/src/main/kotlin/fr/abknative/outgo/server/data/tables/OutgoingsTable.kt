package fr.abknative.outgo.server.data.tables

import org.jetbrains.exposed.v1.core.Expression
import org.jetbrains.exposed.v1.core.QueryBuilder
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone
import java.time.OffsetDateTime

object OutgoingsTable : Table("outgoings") {
    val id = varchar("id", 36)
    val budgetId = varchar("budget_id", 36).references(BudgetsTable.id)
    val userId = varchar("user_id", 128).references(UsersTable.id)
    val name = varchar("name", 255)
    val amountInCents = long("amount_in_cents")
    val recurrence = varchar("recurrence", 50)
    val dueDay = integer("due_day")
    val dueMonth = integer("due_month").nullable()

    val createdAt = timestampWithTimeZone("created_at")
    val updatedAt = timestampWithTimeZone("updated_at")
    val serverUpdatedAt = timestampWithTimeZone("server_updated_at")
        .defaultExpression(object : Expression<OffsetDateTime>() {
            override fun toQueryBuilder(queryBuilder: QueryBuilder) {
                queryBuilder.append("CURRENT_TIMESTAMP")
            }
        })

    val isDeleted = bool("is_deleted").default(false)

    override val primaryKey = PrimaryKey(id)
}