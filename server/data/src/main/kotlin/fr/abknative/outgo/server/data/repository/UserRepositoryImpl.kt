package fr.abknative.outgo.server.data.repository

import fr.abknative.outgo.server.core.repository.UserRepository
import fr.abknative.outgo.server.data.tables.UsersTable
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.upsert

class UserRepositoryImpl : UserRepository {

    /**
     * S'assure que l'utilisateur existe dans la base avant d'insérer ses données.
     */
    override fun ensureUserExists(userId: String, email: String) {
        transaction {
            UsersTable.upsert { row ->
                row[id] = userId
                row[this.email] = email
            }
        }
    }
}