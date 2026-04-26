package fr.abknative.outgo.android.ui.operation

import androidx.compose.runtime.Composable
import fr.abknative.outgo.shared.core.ui.resources.*
import org.jetbrains.compose.resources.stringResource

internal object OperationLabels {

    val SHEET_TITLE_ADD @Composable get() = stringResource(Res.string.form_sheet_title_add)
    val SHEET_TITLE_EDIT @Composable get() = stringResource(Res.string.form_sheet_title_edit)
    val FIELD_NAME @Composable get() = stringResource(Res.string.form_field_name)
    val FIELD_PLACE_HOLDER_NAME @Composable get() = stringResource(Res.string.form_field_place_holder_name)
    val FIELD_AMOUNT @Composable get() = stringResource(Res.string.form_field_amount)
    val FIELD_PLACE_HOLDER_AMOUNT @Composable get() = stringResource(Res.string.form_field_place_holder_amount)
    val FIELD_START_DATE_LABEL @Composable get() = stringResource(Res.string.form_field_start_date_label)
    val FIELD_END_DATE_LABEL @Composable get() = stringResource(Res.string.form_field_end_date_label)
    val FIELD_RECURRENCE_DESC @Composable get() = stringResource(Res.string.form_field_recurrence_desc)
    val FIELD_TYPE_DESC @Composable get() = stringResource(Res.string.form_field_type_desc)
    val TYPE_EXPENSE @Composable get() = stringResource(Res.string.form_type_expense)
    val TYPE_INCOME @Composable get() = stringResource(Res.string.form_type_income)
}