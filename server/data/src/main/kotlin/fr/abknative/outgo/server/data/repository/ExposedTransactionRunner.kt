package fr.abknative.outgo.server.data.repository

import fr.abknative.outgo.server.core.repository.TransactionRunner
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class ExposedTransactionRunner : TransactionRunner {
    override fun <T> invoke(block: () -> T): T {
        return transaction {
            block()
        }
    }
}