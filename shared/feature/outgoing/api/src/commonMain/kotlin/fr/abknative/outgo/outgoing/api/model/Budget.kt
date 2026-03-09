package fr.abknative.outgo.outgoing.api.model

data class Budget(
    val id: String = "default",
    val monthlyIncomeInCents: Long = 0L
)