package fr.abknative.outgo.wallet.api.usecase

import fr.abknative.outgo.wallet.api.model.Outgoing

/**
 * Calculates the total sum of a given list of expenses.
 */
interface CalculateTotalOutgoingsUseCase {

    /**
     * Computes the total amount.
     *
     * @param outgoings The list of expenses to sum up.
     * @return The total amount in cents.
     */
    operator fun invoke(outgoings: List<Outgoing>): Long
}