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

        /*

        <string name="error_wallet_empty_name">Le nom du compte ne peut pas être vide.</string>
        <string name="error_wallet_not_found">Le compte avec l'identifiant %s n'a pas été trouvé.</string>

        <string name="error_operation_empty_name">L'intitulé de l'opération ne peut pas être vide.</string>
        <string name="error_operation_invalid_amount">Le montant doit être strictement supérieur à 0.</string>
        <string name="error_operation_not_found">L'opération avec l'identifiant %s n'a pas été trouvée.</string>
        <string name="error_operation_invalid_date_order">La date de fin ne peut pas être antérieure à la date de début.</string>
        <string name="error_operation_wallet_not_found">Le compte associé à cette opération est introuvable.</string>
        <string name="error_operation_unknow_cycle">Le cycle de récurrence choisi n'est pas supporté.</string>

        <string name="error_auth_invalid_credentials">L'email ou le mot de passe est incorrect.</string>
        <string name="error_auth_user_not_found">Aucun compte n'est associé à cet email.</string>
        <string name="error_auth_session_expired">Votre session a expiré. Veuillez vous reconnecter.</string>

        <string name="error_global_network">Impossible de se connecter. Vérifiez votre connexion internet et réessayez.</string>
        <string name="error_global_server">Le serveur est actuellement indisponible. Veuillez réessayer plus tard.</string>
        <string name="error_global_unauthorized">Accès refusé. Veuillez vérifier vos identifiants.</string>
        <string name="error_global_database">Un problème technique empêche l'enregistrement de vos données. Si le problème persiste, redémarrez l'application.</string>
        <string name="error_global_unknown">Oups ! Quelque chose s'est mal passé. Nous faisons de notre mieux pour corriger ça.</string>

         */


        // Domaine : Portefeuille (Wallet)
        is WalletError.EmptyName -> stringResource(Res.string.error_wallet_empty_name)
        is WalletError.NotFound -> stringResource(Res.string.error_wallet_not_found, id)

        // Domaine : Opération (Operation)
        is OperationError.EmptyName -> stringResource(Res.string.error_operation_empty_name)
        is OperationError.InvalidAmount -> stringResource(Res.string.error_operation_invalid_amount)
        is OperationError.NotFound -> stringResource(Res.string.error_operation_not_found, id)
        is OperationError.InvalidDateOrder -> stringResource(Res.string.error_operation_invalid_date_order)
        is OperationError.WalletNotFound -> stringResource(Res.string.error_operation_wallet_not_found, walletId)
        is OperationError.UnknownCycle -> stringResource(Res.string.error_operation_unknow_cycle)

        // Domaine : Authentification
        is AuthError.InvalidCredentials -> stringResource(Res.string.error_auth_invalid_credentials)
        is AuthError.UserNotFound -> stringResource(Res.string.error_auth_user_not_found)
        is AuthError.SessionExpired -> stringResource(Res.string.error_auth_session_expired)

        // Domaine : Global / Technique
        is CommonError.NetworkError -> stringResource(Res.string.error_global_network)
        is CommonError.ServerError -> stringResource(Res.string.error_global_server)
        is CommonError.Unauthorized -> stringResource(Res.string.error_global_unauthorized)
        is CommonError.DatabaseError -> stringResource(Res.string.error_global_database)
        is CommonError.UnknownError -> stringResource(Res.string.error_global_unknown)

        else -> stringResource(Res.string.error_global_unknown)
    }
}