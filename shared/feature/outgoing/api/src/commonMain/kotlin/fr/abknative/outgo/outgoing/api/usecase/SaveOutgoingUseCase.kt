package fr.abknative.outgo.outgoing.api.usecase

import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.core.api.Result
import fr.abknative.outgo.outgoing.api.Recurrence

interface SaveOutgoingUseCase {
    suspend operator fun invoke(
        id: String? = null,
        name: String,
        amountInCents: Long,
        recurrence: Recurrence,
        dueDay: Int,
        dueMonth: Int? = null
    ): Result<Unit, AppException>
}