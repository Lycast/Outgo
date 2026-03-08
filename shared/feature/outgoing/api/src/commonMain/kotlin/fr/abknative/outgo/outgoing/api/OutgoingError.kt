package fr.abknative.outgo.outgoing.api

import fr.abknative.outgo.core.api.AppException

sealed class OutgoingError(message: String? = null) : AppException(message) {
    class InvalidAmount : OutgoingError("Amount must be greater than zero")
    class EmptyName : OutgoingError("Outgoing name cannot be empty")
    class NotFound(val id: String) : OutgoingError("Outgoing item with id $id not found")
    class InvalidDate : OutgoingError("The provided billing date or month is invalid")
    class UnknownCycle : OutgoingError("The selected billing cycle is not supported")
}