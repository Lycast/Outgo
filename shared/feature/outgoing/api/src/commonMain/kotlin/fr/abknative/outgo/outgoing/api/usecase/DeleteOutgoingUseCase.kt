package fr.abknative.outgo.outgoing.api.usecase

import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.core.api.Result

/**
 * Handles the deletion of an outgoing expense.
 * Performs a logical (soft) deletion to support offline-first synchronization,
 * rather than a hard removal from the local database.
 */
interface DeleteOutgoingUseCase {

    /**
     * Executed to soft-delete the specified record.
     *
     * @param id The unique identifier of the expense to delete.
     * @return A [Result] indicating success or an [AppException] on failure.
     */
    suspend operator fun invoke(id: String): Result<Unit, AppException>
}