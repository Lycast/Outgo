package fr.abknative.outgo.server.data.tables

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone

object BudgetsTable : Table("budgets") {
    val id = varchar("id", 36)
    val userId = varchar("user_id", 128).references(UsersTable.id)
    val monthlyIncomeInCents = long("monthly_income_in_cents").default(0L)
    val createdAt = timestampWithTimeZone("created_at")
    val updatedAt = timestampWithTimeZone("updated_at")
    val serverUpdatedAt = timestamp("server_updated_at")
        .defaultExpression(CurrentTimestamp)

    override val primaryKey = PrimaryKey(id)
}