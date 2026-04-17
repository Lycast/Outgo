package fr.abknative.outgo.wallet.api.model.presenter

/**
 * Aggregates the calculated financial metrics required to render the dashboard UI.
 */
data class PeriodStats(
    val currentBalanceInCents: Long,
    val totalIncomesInCents: Long,
    val totalExpensesInCents: Long,
    val remainingToPayInCents: Long,
    val disposableIncomeInCents: Long
)