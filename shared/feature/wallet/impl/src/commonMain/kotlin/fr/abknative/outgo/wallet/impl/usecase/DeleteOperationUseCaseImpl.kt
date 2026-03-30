package fr.abknative.outgo.wallet.impl.usecase

import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.Result
import fr.abknative.outgo.wallet.api.repository.OutgoingRepository
import fr.abknative.outgo.wallet.api.usecase.DeleteOperationUseCase

internal class DeleteOperationUseCaseImpl(
    private val repository: OutgoingRepository
) : DeleteOperationUseCase {

    override suspend fun invoke(id: String): Result<Unit, AppException> {
        return repository.markAsDeleted(id)
    }
}