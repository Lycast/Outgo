package fr.abknative.outgo.android.ui

import androidx.compose.runtime.Composable
import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.core.api.CommonError
import fr.abknative.outgo.outgoing.api.OutgoingError
import fr.abknative.outgo.shared.core.ui.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppException.toUIString(): String {
    return when (this) {
        is OutgoingError.EmptyName -> stringResource(Res.string.error_outgoing_empty_name)
        is OutgoingError.InvalidAmount -> stringResource(Res.string.error_outgoing_invalid_amount)
        is CommonError.NetworkError -> stringResource(Res.string.error_global_network)
        else -> stringResource(Res.string.error_global_unknown)
    }
}