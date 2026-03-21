package fr.abknative.outgo.outgoing.api.usecase

import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.core.api.Result

/**
 * Updates the user's monthly income or baseline budget.
 * Functions as an "upsert" operation: it safely creates a new budget entry if none exists,
 * or updates the existing one.
 */
interface UpdateIncomeUseCase {

    /**
     * Executes the income update operation.
     *
     * @param amountInCents The new income amount to be set, in cents.
     * @param budgetId The identifier of the budget to update (defaults to "default").
     * @return A [Result] indicating success or containing an [AppException] on failure.
     */
    suspend operator fun invoke(amountInCents: Long, budgetId: String = "default"): Result<Unit, AppException>
}