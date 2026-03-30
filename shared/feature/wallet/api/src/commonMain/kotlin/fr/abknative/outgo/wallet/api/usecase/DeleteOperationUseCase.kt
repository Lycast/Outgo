package fr.abknative.outgo.wallet.api.usecase

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result

/**
 * Handles the deletion of a specific financial operation.
 * Implements a soft-delete mechanism by timestamping the deletion event,
 * ensuring remote synchronization engines can replicate the state.
 */
interface DeleteOperationUseCase {

    /**
     * Executes the soft-delete action for an operation.
     *
     * @param id The unique identifier of the operation to remove.
     * @return A [Result] indicating success or containing an [AppException] on failure.
     */
    suspend operator fun invoke(id: String): Result<Unit, AppException>
}