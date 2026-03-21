package fr.abknative.outgo.outgoing.api.model

/**
 * Represents the user's overall budget.
 *
 * @property id The budget identifier (uses "default" for a single-budget setup).
 * @property monthlyIncomeInCents The monthly income, stored in cents to prevent floating-point precision issues.
 */
data class Budget(
    val id: String = "default",
    val monthlyIncomeInCents: Long = 0L
)