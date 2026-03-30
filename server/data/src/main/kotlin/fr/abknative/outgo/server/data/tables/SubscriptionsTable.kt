@file:OptIn(ExperimentalUuidApi::class)

package fr.abknative.outgo.server.data.tables

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.timestampWithTimeZone
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object SubscriptionsTable : Table("subscriptions") {

    val id = uuid("id").clientDefault { Uuid.random() }

    val userId = varchar("user_id", 128).references(UsersTable.id)

    val status = varchar("status", 20).default("active")
    val paymentSource = varchar("source", 20)
    val externalTransactionId = varchar("external_transaction_id", 255)

    val startDate = timestampWithTimeZone("start_date")
    val endDate = timestampWithTimeZone("end_date")
    val createdAt = timestampWithTimeZone("created_at")

    override val primaryKey = PrimaryKey(id)
}