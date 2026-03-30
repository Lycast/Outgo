package fr.abknative.outgo.wallet.impl.usecase

import fr.abknative.outgo.wallet.api.model.Operation

internal class CalculateTotalExpensesUseCaseImpl : CalculateTotalExpensesUseCase {

    override fun invoke(operations: List<Operation>): Long {
        return operations.sumOf { it.amountInCents }
    }
}