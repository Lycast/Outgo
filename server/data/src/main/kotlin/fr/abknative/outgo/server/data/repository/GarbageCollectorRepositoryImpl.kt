package fr.abknative.outgo.server.data.repository

import fr.abknative.outgo.server.core.repository.GarbageCollectorRepository
import fr.abknative.outgo.server.data.tables.OperationsTable
import fr.abknative.outgo.server.data.tables.WalletsTable
import org.jetbrains.exposed.v1.core.less
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.time.OffsetDateTime
import java.time.ZoneOffset

class GarbageCollectorRepositoryImpl : GarbageCollectorRepository {

    override fun purgeOldDeletedData(daysOld: Long): Pair<Int, Int> {
        val thresholdDate = OffsetDateTime.now(ZoneOffset.UTC).minusDays(daysOld)

        var deletedWallets = 0
        var deletedOperations = 0

        transaction {
            deletedOperations = OperationsTable.deleteWhere {
                OperationsTable.deletedAt less thresholdDate
            }
            deletedWallets = WalletsTable.deleteWhere {
                WalletsTable.deletedAt less thresholdDate
            }
        }

        return Pair(deletedWallets, deletedOperations)
    }
}