package fr.abknative.outgo.android.core

import androidx.compose.runtime.Composable
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.CommonError
import fr.abknative.outgo.shared.core.ui.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppException.toCommonUIString(): String {
    return when (this) {

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