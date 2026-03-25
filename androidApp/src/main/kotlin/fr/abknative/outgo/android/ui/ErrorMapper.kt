package fr.abknative.outgo.android.ui

import androidx.compose.runtime.Composable
import fr.abknative.outgo.core.api.logs.AppException
import fr.abknative.outgo.core.api.logs.CommonError
import fr.abknative.outgo.outgoing.api.OutgoingError
import fr.abknative.outgo.shared.core.ui.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppException.toUIString(): String {
    return when (this) {
        is OutgoingError.EmptyName -> stringResource(Res.string.error_outgoing_empty_name)
        is OutgoingError.InvalidAmount -> stringResource(Res.string.error_outgoing_invalid_amount)
        is OutgoingError.InvalidDate -> stringResource(Res.string.error_outgoing_invalid_date)
        is OutgoingError.NotFound -> stringResource(Res.string.error_outgoing_not_found, id)
        is OutgoingError.UnknownCycle -> stringResource(Res.string.error_outgoing_unknow_cycle)
        is CommonError.NetworkError -> stringResource(Res.string.error_global_network)
        is CommonError.DatabaseError -> stringResource(Res.string.error_global_database)
        is CommonError.UnknownError -> stringResource(Res.string.error_global_unknown)
        else -> stringResource(Res.string.error_global_unknown)
    }
}