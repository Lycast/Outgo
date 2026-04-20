package fr.abknative.outgo.android.ui.operation

import androidx.compose.runtime.Composable
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.shared.core.ui.resources.*
import fr.abknative.outgo.wallet.api.logs.OperationError
import fr.abknative.outgo.wallet.api.logs.WalletError
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppException.toUIString(): String {
    return when (this) {

        // --- Domaine : Portefeuille (Wallet) ---
        is WalletError.EmptyName -> stringResource(Res.string.error_wallet_empty_name)
        is WalletError.NotFound -> stringResource(Res.string.error_wallet_not_found, id)

        // --- Domaine : Opération (Operation) ---
        is OperationError.EmptyName -> stringResource(Res.string.error_operation_empty_name)
        is OperationError.InvalidAmount -> stringResource(Res.string.error_operation_invalid_amount)
        is OperationError.NotFound -> stringResource(Res.string.error_operation_not_found, id)
        is OperationError.InvalidDateOrder -> stringResource(Res.string.error_operation_invalid_date_order)
        is OperationError.WalletNotFound -> stringResource(Res.string.error_operation_wallet_not_found, walletId)
        is OperationError.UnknownCycle -> stringResource(Res.string.error_operation_unknow_cycle)

        // --- Sécurité (Fallback) ---
        else -> stringResource(Res.string.error_global_unknown)
    }
}

