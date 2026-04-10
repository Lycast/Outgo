package fr.abknative.outgo.wallet.api.logs

import fr.abknative.outgo.core.api.logs.AppException

sealed class WalletError : AppException() {
    class EmptyName : WalletError()
    class NotFound(val id: String) : WalletError()
    class NoActiveWallet : WalletError()
}