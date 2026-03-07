package fr.abknative.outgo.android

import androidx.compose.runtime.Composable
import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.core.api.CommonError
import fr.abknative.outgo.outgoing.api.OutgoingError
import androidx.compose.ui.res.stringResource

@Composable
fun AppException.toUIString(): String {
    return when (this) {
        is OutgoingError.EmptyName -> stringResource(R.string.error_outgoing_empty_name)
        is OutgoingError.InvalidAmount -> stringResource(R.string.error_outgoing_invalid_amount)
        is CommonError.NetworkError -> stringResource(R.string.error_global_network)
        else -> stringResource(R.string.error_global_unknown)
    }
}