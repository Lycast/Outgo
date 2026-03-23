package fr.abknative.outgo.server.data.tables

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone

object UsersTable : Table("users") {
    val id = varchar("id", 128)
    val email = varchar("email", 255)
    override val primaryKey = PrimaryKey(id)
}

object BudgetsTable : Table("budgets") {
    val id = varchar("id", 36)
    val userId = varchar("user_id", 128).references(UsersTable.id)
    val monthlyIncomeInCents = integer("monthly_income_in_cents").default(0)

    val serverUpdatedAt = timestamp("server_updated_at")
        .defaultExpression(CurrentTimestamp)

    override val primaryKey = PrimaryKey(id)
}

object OutgoingsTable : Table("outgoings") {
    val id = varchar("id", 36)
    val budgetId = varchar("budget_id", 36).references(BudgetsTable.id)
    val userId = varchar("user_id", 128).references(UsersTable.id)
    val name = varchar("name", 255)
    val amountInCents = integer("amount_in_cents")
    val recurrence = varchar("recurrence", 50)
    val dueDay = integer("due_day")
    val dueMonth = integer("due_month").nullable()

    val createdAt = timestampWithTimeZone("created_at")
    val updatedAt = timestampWithTimeZone("updated_at")
    val serverUpdatedAt = timestamp("server_updated_at")
        .defaultExpression(CurrentTimestamp)

    val isDeleted = bool("is_deleted").default(false)

    override val primaryKey = PrimaryKey(id)
}