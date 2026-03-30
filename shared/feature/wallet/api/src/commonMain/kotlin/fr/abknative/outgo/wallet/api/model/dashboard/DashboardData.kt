package fr.abknative.outgo.wallet.api.model.dashboard

/**
 * Aggregates the calculated financial metrics required to render the dashboard UI.
 */
data class DashboardData(
    val currentBalanceInCents: Long,
    val totalExpensesInCents: Long,
    val remainingToPayInCents: Long,
    val disposableIncomeInCents: Long
)