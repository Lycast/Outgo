package fr.abknative.outgo.android.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import fr.abknative.outgo.android.R
import fr.abknative.outgo.core.api.AppException
import fr.abknative.outgo.core.api.CommonError
import fr.abknative.outgo.outgoing.api.OutgoingError

@Composable
fun AppException.toUIString(): String {
    return when (this) {
        is OutgoingError.EmptyName -> stringResource(R.string.error_outgoing_empty_name)
        is OutgoingError.InvalidAmount -> stringResource(R.string.error_outgoing_invalid_amount)
        is CommonError.NetworkError -> stringResource(R.string.error_global_network)
        else -> stringResource(R.string.error_global_unknown)
    }
}