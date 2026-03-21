package fr.abknative.outgo.outgoing.api.usecase

import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.core.api.Result
import fr.abknative.outgo.outgoing.api.Recurrence

/**
 * Validates and saves an outgoing expense.
 * Acts as a single entry point for both creating new expenses and updating existing ones.
 */
interface SaveOutgoingUseCase {

    /**
     * Executes the save operation.
     *
     * @param id The unique identifier of the expense. If null or blank, a new expense is created.
     * @param name The display name of the expense. Must not be blank.
     * @param amountInCents The expense amount. Must be strictly greater than 0.
     * @param recurrence The frequency of the expense ([Recurrence.MONTHLY] or [Recurrence.YEARLY]).
     * @param dueDay The day of the month the expense is due (1-31).
     * @param dueMonth The specific month the expense is due. Required only if recurrence is YEARLY.
     * @return A [Result] indicating success or containing a specific [OutgoingError] on validation failure.
     */
    suspend operator fun invoke(
        id: String? = null,
        name: String,
        amountInCents: Long,
        recurrence: Recurrence,
        dueDay: Int,
        dueMonth: Int? = null
    ): Result<Unit, AppException>
}