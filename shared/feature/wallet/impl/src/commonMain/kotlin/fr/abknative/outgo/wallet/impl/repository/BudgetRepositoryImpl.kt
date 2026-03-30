package fr.abknative.outgo.wallet.impl.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import fr.abknative.outgo.core.api.AppDispatchers
import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.core.api.logs.*
import fr.abknative.outgo.database.OutgoDatabase
import fr.abknative.outgo.wallet.api.model.Wallet
import fr.abknative.outgo.wallet.api.repository.BudgetRepository
import fr.abknative.outgo.wallet.impl.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class BudgetRepositoryImpl(
    private val database: OutgoDatabase,
    private val dispatchers: AppDispatchers
) : BudgetRepository {

    private val queries = database.budgetQueries
    private val tag = "BudgetLocalRepo"

    override fun observeBudget(id: String): Flow<Wallet?> {
        return queries.getBudgetById(id)
            .asFlow()
            .mapToOneOrNull(dispatchers.io)
            .map { entity -> entity?.toDomain() }
    }

    override suspend fun getBudget(id: String): Result<Wallet?, AppException> = asResult(
        onError = {
            AppLogger.get()?.e(tag, "Failed to fetch budget with id: $id", it)
            CommonError.DatabaseError(it)
        }
    ) {
        queries.getBudgetById(id).executeAsOneOrNull()?.toDomain()
    }

    override suspend fun insert(wallet: Wallet): Result<Unit, AppException> = asResult(
        onError = {
            AppLogger.get()?.e(tag, "Failed to insert budget: ${wallet.id}", it)
            CommonError.DatabaseError(it)
        }
    ) {
        queries.transaction {
            queries.insertBudget(
                id = wallet.id,
                monthlyIncomeInCents = wallet.monthlyIncomeInCents,
                createdAt = wallet.createdAt,
                updatedAt = wallet.updatedAt,
                syncStatus = wallet.syncStatus.name
            )
        }
    }

    override suspend fun update(wallet: Wallet): Result<Unit, AppException> = asResult(
        onError = {
            AppLogger.get()?.e(tag, "Failed to update budget: ${wallet.id}", it)
            CommonError.DatabaseError(it)
        }
    ) {
        queries.transaction {
            queries.updateBudget(
                monthlyIncomeInCents = wallet.monthlyIncomeInCents,
                updatedAt = wallet.updatedAt,
                syncStatus = wallet.syncStatus.name,
                id = wallet.id
            )
        }
    }

    override suspend fun getPendingBudgets(): Result<List<Wallet>, AppException> = asResult(
        onError = {
            AppLogger.get()?.e(tag, "Failed to fetch pending budgets", it)
            CommonError.DatabaseError(it)
        }
    ) {
        queries.getPendingBudgets().executeAsList().map { it.toDomain() }
    }

    override suspend fun updateSyncStatus(
        id: String,
        status: SyncStatus
    ): Result<Unit, AppException> = asResult(
        onError = {
            AppLogger.get()?.e(tag, "Failed to update sync status ($status) for id: $id", it)
            CommonError.DatabaseError(it)
        }
    ) {
        queries.updateSyncStatus(syncStatus = status.name, id = id)
    }

    override suspend fun syncFromServer(wallets: List<Wallet>): Result<Unit, AppException> = asResult(
        onError = {
            AppLogger.get()?.e(tag, "Failed to sync ${wallets.size} budgets from server", it)
            CommonError.DatabaseError(it)
        }
    ) {
        queries.transaction {
            wallets.forEach { remoteBudget ->
                val exists = queries.getBudgetById(remoteBudget.id).executeAsOneOrNull() != null
                if (exists) {
                    queries.updateBudget(
                        monthlyIncomeInCents = remoteBudget.monthlyIncomeInCents,
                        updatedAt = remoteBudget.updatedAt,
                        syncStatus = SyncStatus.SYNCED.name,
                        id = remoteBudget.id
                    )
                } else {
                    queries.insertBudget(
                        id = remoteBudget.id,
                        monthlyIncomeInCents = remoteBudget.monthlyIncomeInCents,
                        createdAt = remoteBudget.createdAt,
                        updatedAt = remoteBudget.updatedAt,
                        syncStatus = SyncStatus.SYNCED.name
                    )
                }
            }
        }
    }
}