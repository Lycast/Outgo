package fr.abknative.outgo.wallet.impl.usecase

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.core.api.model.SyncStatus
import fr.abknative.outgo.core.api.time.EpochMillis
import fr.abknative.outgo.wallet.api.logs.OperationError
import fr.abknative.outgo.wallet.api.model.Operation
import fr.abknative.outgo.wallet.api.model.operation.OperationType
import fr.abknative.outgo.wallet.api.model.operation.Recurrence
import fr.abknative.outgo.wallet.api.repository.OperationRepository
import fr.abknative.outgo.wallet.api.usecase.SaveOperationUseCase

internal class SaveOperationUseCaseImpl(
    private val repository: OperationRepository
) : SaveOperationUseCase {

    override suspend fun invoke(
        id: String?,
        walletId: String,
        name: String,
        amountInCents: Long,
        type: OperationType,
        recurrence: Recurrence,
        startDate: EpochMillis,
        endDate: EpochMillis?
    ): Result<Unit, AppException> {

        val cleanName = name.trim()

        when {
            cleanName.isBlank() -> return Result.Error(OperationError.EmptyName())
            amountInCents <= 0 -> return Result.Error(OperationError.InvalidAmount())
            walletId.isBlank() -> return Result.Error(OperationError.WalletNotFound(walletId))
            recurrence == Recurrence.UNKNOWN -> return Result.Error(OperationError.UnknownCycle())
            endDate != null && endDate < startDate -> return Result.Error(OperationError.InvalidDateOrder())
        }

        val operationPayload = Operation(
            id = id ?: "",
            walletId = walletId,
            name = cleanName,
            amountInCents = amountInCents,
            type = type,
            recurrence = recurrence,
            startDate = startDate,
            endDate = endDate,
            createdAt = 0L,
            updatedAt = 0L,
            deletedAt = null,
            syncStatus = SyncStatus.PENDING_CREATE
        )

        return repository.save(operationPayload)
    }
}