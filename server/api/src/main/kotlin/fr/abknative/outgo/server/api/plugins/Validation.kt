package fr.abknative.outgo.server.api.plugins

import fr.abknative.outgo.outgoing.network.dto.BudgetNetworkDto
import fr.abknative.outgo.outgoing.network.dto.OutgoingNetworkDto
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureValidation() {
    install(RequestValidation) {
        validate<BudgetNetworkDto> { budget ->
            when {
                budget.id.isBlank() -> ValidationResult.Invalid("Budget ID cannot be empty")
                budget.monthlyIncomeInCents < 0 -> ValidationResult.Invalid("Income cannot be negative")
                else -> ValidationResult.Valid
            }
        }

        validate<OutgoingNetworkDto> { outgoing ->
            when {
                outgoing.id.isBlank() -> ValidationResult.Invalid("Outgoing ID cannot be empty")
                outgoing.name.isBlank() -> ValidationResult.Invalid("Name cannot be blank")
                outgoing.name.length > 255 -> ValidationResult.Invalid("Name is too long (max 255)")
                outgoing.amountInCents < 0 -> ValidationResult.Invalid("Amount cannot be negative")
                outgoing.dueDay !in 1..31 -> ValidationResult.Invalid("dueDay must be between 1 and 31")
                outgoing.dueMonth != null && outgoing.dueMonth !in 1..12 ->
                    ValidationResult.Invalid("dueMonth must be between 1 and 12")
                else -> ValidationResult.Valid
            }
        }
    }
}