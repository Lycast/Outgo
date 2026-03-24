package fr.abknative.outgo.server.data.tables

import org.jetbrains.exposed.v1.core.Table

object UsersTable : Table("users") {
    val id = varchar("id", 128)
    val email = varchar("email", 255)
    override val primaryKey = PrimaryKey(id)
}