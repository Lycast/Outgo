package fr.abknative.outgo.wallet.api.logs

import fr.abknative.outgo.core.api.logs.AppException

sealed class OperationError : AppException() {
    class EmptyName : OperationError()
    class InvalidAmount : OperationError()
    class NotFound(val id: String) : OperationError()
    class InvalidDateOrder : OperationError()
    class WalletNotFound(val walletId: String) : OperationError()
    class UnknownCycle : OperationError()

}