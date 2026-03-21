package fr.abknative.outgo.outgoing.api.usecase

import fr.abknative.outgo.outgoing.api.model.Outgoing

/**
 * Calculates the total amount of outgoings that are yet to be paid for a specific month.
 */
interface CalculateRemainingToPayUseCase {

    /**
     * Computes the remaining balance based on the [selectedMonth] compared to the current real date.
     *
     * @param outgoings The list of expenses scheduled for the selected month.
     * @param selectedMonth The month being viewed by the user.
     * @return The sum of remaining expenses in cents.
     */
    operator fun invoke(outgoings: List<Outgoing>, selectedMonth: Int): Long
}