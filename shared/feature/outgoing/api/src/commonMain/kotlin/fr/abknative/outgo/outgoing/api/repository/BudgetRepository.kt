package fr.abknative.outgo.outgoing.api.repository

import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.core.api.Result
import fr.abknative.outgo.outgoing.api.model.Budget
import kotlinx.coroutines.flow.Flow

/**
 * Manages the global financial context (Income).
 */
interface BudgetRepository {

    /**
     * Emits the budget reactively.
     * Retourne un Flow<Budget?> car le budget "default" peut ne pas encore exister au premier lancement.
     */
    fun observeBudget(id: String = "default"): Flow<Budget?>

    /**
     * Retrieves the current budget to check its existence.
     */
    suspend fun getBudget(id: String = "default"): Result<Budget?, AppException>

    /**
     * Inserts the initial budget into the database.
     */
    suspend fun insert(budget: Budget): Result<Unit, AppException>

    /**
     * Updates an existing budget.
     */
    suspend fun update(budget: Budget): Result<Unit, AppException>
}