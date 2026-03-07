package fr.abknative.outgo.outgoing.impl.usecase

import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.core.api.Result
import fr.abknative.outgo.outgoing.api.repository.OutgoingRepository
import fr.abknative.outgo.outgoing.api.usecase.DeleteOutgoingUseCase

internal class DeleteOutgoingUseCaseImpl(
    private val repository: OutgoingRepository
) : DeleteOutgoingUseCase {

    override suspend fun invoke(id: String): Result<Unit, AppException> {
        return repository.markAsDeleted(id)
    }
}