package fr.abknative.outgo.outgoing.impl.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import fr.abknative.outgo.core.api.*
import fr.abknative.outgo.database.OutgoDatabase
import fr.abknative.outgo.outgoing.api.model.Budget
import fr.abknative.outgo.outgoing.api.repository.BudgetRepository
import fr.abknative.outgo.outgoing.impl.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class BudgetRepositoryImpl(
    database: OutgoDatabase,
    private val dispatchers: AppDispatchers
) : BudgetRepository {

    private val queries = database.budgetQueries

    override fun observeBudget(id: String): Flow<Budget?> {
        return queries.getBudgetById(id)
            .asFlow()
            .mapToOneOrNull(dispatchers.io)
            .map { entity -> entity?.toDomain() }
    }

    override suspend fun getBudget(id: String): Result<Budget?, AppException> = asResult(
        onError = { CommonError.DatabaseError(it) }
    ) {
        queries.getBudgetById(id).executeAsOneOrNull()?.toDomain()
    }

    override suspend fun insert(budget: Budget): Result<Unit, AppException> = asResult(
        onError = { CommonError.DatabaseError(it) }
    ) {
        queries.insertBudget(
            id = budget.id,
            monthlyIncomeInCents = budget.monthlyIncomeInCents
        )
    }

    override suspend fun update(budget: Budget): Result<Unit, AppException> = asResult(
        onError = { CommonError.DatabaseError(it) }
    ) {
        queries.updateBudget(
            monthlyIncomeInCents = budget.monthlyIncomeInCents,
            id = budget.id
        )
    }
}