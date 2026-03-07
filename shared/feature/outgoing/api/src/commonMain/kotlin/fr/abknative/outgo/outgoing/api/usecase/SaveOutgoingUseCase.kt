package fr.abknative.outgo.outgoing.api.usecase

import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.outgoing.api.BillingCycle
import fr.abknative.outgo.core.api.Result

interface SaveOutgoingUseCase {
    suspend operator fun invoke(
        name: String,
        amountInCents: Long,
        cycle: BillingCycle,
        billingDay: Int
    ): Result<Unit, AppException>
}