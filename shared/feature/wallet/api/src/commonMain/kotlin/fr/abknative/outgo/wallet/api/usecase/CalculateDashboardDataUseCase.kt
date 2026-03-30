package fr.abknative.outgo.wallet.api.usecase

import fr.abknative.outgo.wallet.api.model.Operation

/**
 * Aggregates the calculated financial metrics required to render the dashboard UI.
 */
data class DashboardData(
    val currentBalanceInCents: Long,
    val totalExpensesInCents: Long,
    val remainingToPayInCents: Long,
    val disposableIncomeInCents: Long
)

/**
 * Encapsulates the core financial algorithms.
 * Acts as a Strategy pattern context, allowing different calculation engines
 * (e.g., Basic vs. Premium Cashflow) to process the raw operations into unified [DashboardData].
 */
interface CalculateDashboardDataUseCase {
    /**
     * Computes the financial metrics for the requested period.
     *
     * @param operations The raw list of operations to process.
     * @param currentMonth The month currently viewed by the user.
     * @param currentYear The year currently viewed by the user.
     * @return The aggregated [DashboardData] ready for UI consumption.
     */
    operator fun invoke(operations: List<Operation>, currentMonth: Int, currentYear: Int): DashboardData
}