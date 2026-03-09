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
     * Emits the budget (containing the income) reactively.
     * Use "default" as ID for the MVP.
     */
    fun observeBudget(id: String = "default"): Flow<Budget>

    /**
     * Updates the reference monthly income.
     */
    suspend fun updateMonthlyIncome(
        amountInCents: Long,
        budgetId: String = "default"
    ): Result<Unit, AppException>
}