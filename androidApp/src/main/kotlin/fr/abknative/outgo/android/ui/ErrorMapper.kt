package fr.abknative.outgo.android.ui

import androidx.compose.runtime.Composable
import fr.abknative.outgo.auth.api.AuthError
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.CommonError
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

        // --- Domaine : Authentification ---
        is AuthError.InvalidCredentials -> stringResource(Res.string.error_auth_invalid_credentials)
        is AuthError.UserNotFound -> stringResource(Res.string.error_auth_user_not_found)
        is AuthError.SessionExpired -> stringResource(Res.string.error_auth_session_expired)
        is AuthError.NeedsReauthentication -> stringResource(Res.string.error_auth_needs_reauth)
        is AuthError.DataConflict -> stringResource(Res.string.error_auth_data_conflict)

        // --- Domaine : Global / Technique ---
        is CommonError.NetworkError -> stringResource(Res.string.error_global_network)
        is CommonError.Timeout -> stringResource(Res.string.error_global_timeout)
        is CommonError.ServerError -> stringResource(Res.string.error_global_server)
        is CommonError.Unauthorized -> stringResource(Res.string.error_global_unauthorized)
        is CommonError.DatabaseError -> stringResource(Res.string.error_global_database)
        is CommonError.UnknownError -> stringResource(Res.string.error_global_unknown)

        // --- Sécurité (Fallback) ---
        else -> stringResource(Res.string.error_global_unknown)
    }
}