package fr.abknative.outgo.server.api.plugins

import fr.abknative.outgo.wallet.network.dto.OperationNetworkDto
import fr.abknative.outgo.wallet.network.dto.WalletNetworkDto
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureValidation() {
    install(RequestValidation) {
        validate<WalletNetworkDto> { wallet ->
            when {
                wallet.id.isBlank() -> ValidationResult.Invalid("Wallet ID cannot be empty")
                wallet.name.isBlank() -> ValidationResult.Invalid("Wallet name cannot be empty")
                else -> ValidationResult.Valid
            }
        }

        validate<OperationNetworkDto> { operation ->
            when {
                operation.id.isBlank() -> ValidationResult.Invalid("Operation ID cannot be empty")
                operation.name.isBlank() -> ValidationResult.Invalid("Name cannot be blank")
                operation.name.length > 255 -> ValidationResult.Invalid("Name is too long (max 255)")
                operation.amountInCents < 0 -> ValidationResult.Invalid("Amount cannot be negative")
                operation.type != "INCOME" && operation.type != "EXPENSE" -> ValidationResult.Invalid("Invalid operation type")
                else -> ValidationResult.Valid
            }
        }
    }
}