package fr.abknative.outgo.wallet.api.usecase

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.core.api.time.EpochMillis
import fr.abknative.outgo.wallet.api.model.operation.OperationType
import fr.abknative.outgo.wallet.api.model.operation.Recurrence

/**
 * Validates and saves a financial operation (income or expense).
 * Serves as the single entry point for creating or updating cash flow records.
 */
interface SaveOperationUseCase {

    /**
     * Executes the save operation.
     *
     * @param id The unique identifier. If null or blank, a new operation is created.
     * @param walletId The identifier of the parent wallet.
     * @param name The display name of the operation. Must not be blank.
     * @param amountInCents The monetary amount. Must be strictly greater than 0.
     * @param type The financial direction ([OperationType.INCOME] or [OperationType.EXPENSE]).
     * @param recurrence The frequency cycle of the operation.
     * @param startDate The absolute temporal anchor (EpochMillis) for the first occurrence.
     * @param endDate The optional temporal anchor (EpochMillis) marking the end of a recurring operation.
     * @return A [Result] indicating success or containing an [AppException] on validation failure.
     */
    suspend operator fun invoke(
        id: String? = null,
        walletId: String,
        name: String,
        amountInCents: Long,
        type: OperationType,
        recurrence: Recurrence,
        startDate: EpochMillis,
        endDate: EpochMillis? = null
    ): Result<Unit, AppException>
}