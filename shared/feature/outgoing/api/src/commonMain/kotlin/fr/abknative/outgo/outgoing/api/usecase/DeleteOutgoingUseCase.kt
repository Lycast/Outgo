package fr.abknative.outgo.outgoing.api.usecase

import fr.abknative.outgo.core.api.Result
import fr.abknative.outgo.core.api.AppException

interface DeleteOutgoingUseCase {
    suspend operator fun invoke(id: String): Result<Unit, AppException>
}