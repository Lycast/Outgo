package fr.abknative.outgo.server.data.tables

import org.jetbrains.exposed.v1.core.Expression
import org.jetbrains.exposed.v1.core.QueryBuilder
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone
import java.time.OffsetDateTime

object WalletsTable : Table("wallets") {
    val id = varchar("id", 36)
    val userId = varchar("user_id", 128).references(UsersTable.id)
    val name = varchar("name", 255)

    // Timestamps synchronisation
    val createdAt = timestampWithTimeZone("created_at")
    val updatedAt = timestampWithTimeZone("updated_at")
    val deletedAt = timestampWithTimeZone("deleted_at").nullable()

    // Le juge de paix du serveur pour le PULL
    val serverUpdatedAt = timestampWithTimeZone("server_updated_at")
        .defaultExpression(object : Expression<OffsetDateTime>() {
            override fun toQueryBuilder(queryBuilder: QueryBuilder) {
                queryBuilder.append("CURRENT_TIMESTAMP")
            }
        })

    override val primaryKey = PrimaryKey(id)
}