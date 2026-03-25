package fr.abknative.outgo.outgoing.api

import fr.abknative.outgo.core.api.logs.AppException

sealed class OutgoingError() : AppException() {
    class EmptyName : OutgoingError()
    class InvalidAmount : OutgoingError()
    class NotFound(val id: String) : OutgoingError()
    class InvalidDate : OutgoingError()
    class UnknownCycle : OutgoingError()
}