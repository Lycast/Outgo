package fr.abknative.outgo.wallet.impl.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import fr.abknative.outgo.core.api.AppDispatchers
import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.core.api.logs.*
import fr.abknative.outgo.database.OutgoDatabase
import fr.abknative.outgo.wallet.api.model.Budget
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

    override fun observeBudget(id: String): Flow<Budget?> {
        return queries.getBudgetById(id)
            .asFlow()
            .mapToOneOrNull(dispatchers.io)
            .map { entity -> entity?.toDomain() }
    }

    override suspend fun getBudget(id: String): Result<Budget?, AppException> = asResult(
        onError = {
            AppLogger.get()?.e(tag, "Failed to fetch budget with id: $id", it)
            CommonError.DatabaseError(it)
        }
    ) {
        queries.getBudgetById(id).executeAsOneOrNull()?.toDomain()
    }

    override suspend fun insert(budget: Budget): Result<Unit, AppException> = asResult(
        onError = {
            AppLogger.get()?.e(tag, "Failed to insert budget: ${budget.id}", it)
            CommonError.DatabaseError(it)
        }
    ) {
        queries.transaction {
            queries.insertBudget(
                id = budget.id,
                monthlyIncomeInCents = budget.monthlyIncomeInCents,
                createdAt = budget.createdAt,
                updatedAt = budget.updatedAt,
                syncStatus = budget.syncStatus.name
            )
        }
    }

    override suspend fun update(budget: Budget): Result<Unit, AppException> = asResult(
        onError = {
            AppLogger.get()?.e(tag, "Failed to update budget: ${budget.id}", it)
            CommonError.DatabaseError(it)
        }
    ) {
        queries.transaction {
            queries.updateBudget(
                monthlyIncomeInCents = budget.monthlyIncomeInCents,
                updatedAt = budget.updatedAt,
                syncStatus = budget.syncStatus.name,
                id = budget.id
            )
        }
    }

    override suspend fun getPendingBudgets(): Result<List<Budget>, AppException> = asResult(
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

    override suspend fun syncFromServer(budgets: List<Budget>): Result<Unit, AppException> = asResult(
        onError = {
            AppLogger.get()?.e(tag, "Failed to sync ${budgets.size} budgets from server", it)
            CommonError.DatabaseError(it)
        }
    ) {
        queries.transaction {
            budgets.forEach { remoteBudget ->
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