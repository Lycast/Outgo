package fr.abknative.outgo.outgoing.api.usecase

import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.core.api.Result

interface UpdateIncomeUseCase {
    suspend operator fun invoke(amountInCents: Long, budgetId: String = "default"): Result<Unit, AppException>
}