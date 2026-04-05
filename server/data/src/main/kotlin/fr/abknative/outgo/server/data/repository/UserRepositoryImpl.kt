package fr.abknative.outgo.server.data.repository

import fr.abknative.outgo.server.core.repository.UserRepository
import fr.abknative.outgo.server.data.tables.OperationsTable
import fr.abknative.outgo.server.data.tables.SubscriptionsTable
import fr.abknative.outgo.server.data.tables.UsersTable
import fr.abknative.outgo.server.data.tables.WalletsTable
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import org.jetbrains.exposed.v1.jdbc.upsert

class UserRepositoryImpl : UserRepository {

    /**
     * Ensures the user exists in the database. Updates the email if already present.
     */
    override fun ensureUserExists(userId: String, email: String) {
        UsersTable.upsert { row ->
            row[id] = userId
            row[this.email] = email
        }
    }

    /**
     * Processes user deletion.
     * If the user has a subscription history, their account is anonymized to retain legal billing records.
     * Otherwise, the account and all related data are hard-deleted.
     */
    override fun deleteUser(userId: String) {
        val hasSubscriptions = SubscriptionsTable.selectAll()
            .where { SubscriptionsTable.userId eq userId }
            .count() > 0

        if (hasSubscriptions) {
            OperationsTable.deleteWhere { OperationsTable.userId eq userId }
            WalletsTable.deleteWhere { WalletsTable.userId eq userId }

            UsersTable.update({ UsersTable.id eq userId }) {
                it[email] = "anonymized@outgo.app"
            }
        } else {
            UsersTable.deleteWhere { UsersTable.id eq userId }
        }
    }
}