package fr.abknative.outgo.outgoing.api.repository

import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.core.api.Result
import fr.abknative.outgo.outgoing.api.model.Budget
import kotlinx.coroutines.flow.Flow

/**
 * Manages the global financial context, primarily the user's income and budget settings.
 */
interface BudgetRepository {

    /**
     * Emits the budget reactively.
     * * @param id The budget identifier to observe.
     * @return A [Flow] that can emit null if the budget hasn't been created yet on first launch.
     */
    fun observeBudget(id: String = "default"): Flow<Budget?>

    /**
     * Performs a one-shot retrieval of the current budget, typically used for initial existence checks.
     *
     * @param id The budget identifier to retrieve.
     * @return A [Result] containing the [Budget] if found, or null.
     */
    suspend fun getBudget(id: String = "default"): Result<Budget?, AppException>

    /**
     * Inserts a new budget into the database.
     *
     * @param budget The [Budget] to insert.
     * @return A [Result] indicating success or an [AppException] on failure.
     */
    suspend fun insert(budget: Budget): Result<Unit, AppException>

    /**
     * Updates an already existing budget.
     *
     * @param budget The [Budget] containing the updated values.
     * @return A [Result] indicating success or an [AppException] on failure.
     */
    suspend fun update(budget: Budget): Result<Unit, AppException>
}