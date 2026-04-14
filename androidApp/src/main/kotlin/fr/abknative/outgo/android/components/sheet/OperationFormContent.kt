package fr.abknative.outgo.android.components.sheet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import fr.abknative.outgo.android.components.operation.OperationTypeSelector
import fr.abknative.outgo.android.components.operation.RecurrenceSelector
import fr.abknative.outgo.android.designsystem.components.buttons.AppButton
import fr.abknative.outgo.android.designsystem.components.buttons.AppTextButton
import fr.abknative.outgo.android.designsystem.components.feedback.FormattedDateInput
import fr.abknative.outgo.android.designsystem.components.inputs.AppTextField
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.android.ui.FormLabels
import fr.abknative.outgo.android.ui.helpers.rememberAmountInputManager
import fr.abknative.outgo.android.ui.helpers.rememberDateInputManager
import fr.abknative.outgo.android.ui.helpers.rememberNameInputManager
import fr.abknative.outgo.core.api.TimeProvider
import fr.abknative.outgo.operation.api.OperationIntent
import fr.abknative.outgo.operation.api.OperationState
import org.koin.compose.koinInject

@Composable
fun OperationFormContent(
    modifier: Modifier = Modifier,
    state: OperationState,
    isPremium: Boolean,
    onIntent: (OperationIntent) -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit,
) {
    val lockSheetConnection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset =
                available
        }
    }

    // --- Configuration & Managers ---
    val isEditMode = state.operationId != null
    val timeProvider = koinInject<TimeProvider>()

    val nameManager = rememberNameInputManager(
        initialValue = state.name,
        onValidChange = { onIntent(OperationIntent.UpdateName(it)) }
    )

    val amountManager = rememberAmountInputManager(
        initialValue = state.amount,
        onValidChange = { onIntent(OperationIntent.UpdateAmount(it)) }
    )

    val dateManager = rememberDateInputManager(
        initialDate = state.date,
        timeProvider = timeProvider,
        onValidDateDerived = { onIntent(OperationIntent.UpdateDate(it)) }
    )


    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.dimens.large)
            .nestedScroll(lockSheetConnection)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.large),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Header Title ---
        Text(
            text = if (isEditMode) FormLabels.SHEET_TITLE_EDIT else FormLabels.SHEET_TITLE_ADD,
            style = AppTheme.typo.subtitle,
            color = AppTheme.colors.textPrimary.toColor(),
            modifier = Modifier.padding(top = AppTheme.dimens.medium)
        )

        // --- Field: Name ---
        AppTextField(
            value = state.name,
            onValueChange = { nameManager.onTextChange(it) },
            label = FormLabels.FIELD_NAME,
            placeholder = FormLabels.FIELD_PLACE_HOLDER_NAME,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            )
        )

        // --- Field: Amount ---
        AppTextField(
            value = amountManager.text,
            onValueChange = { amountManager.onTextChange(it) },
            label = FormLabels.FIELD_AMOUNT,
            placeholder = FormLabels.FIELD_PLACE_HOLDER_AMOUNT,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
            suffix = {
                Text(
                    text = CommonLabels.CURRENCY_SYMBOL,
                    style = AppTheme.typo.body,
                    color = AppTheme.colors.textSecondary.toColor()
                )
            }
        )

        // --- Field: Date ---
        FormattedDateInput(
            value = dateManager.textBuffer,
            onValueChange = { dateManager.onTextChange(it) },
            onDateSelected = { dateManager.onExternalDateSelected(it) },
            initialDateMillis = state.date,
            label = FormLabels.FIELD_DATE_LABEL,
            isError = dateManager.isError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = if (isPremium) ImeAction.Next else ImeAction.Done
            )
        )

        // --- Field: Recurrence ---
        RecurrenceSelector(
            selectedRecurrence = state.recurrence,
            onRecurrenceChanged = { onIntent(OperationIntent.UpdateRecurrence(it)) }
        )

        // --- Field: Type (Premium Only) ---
        if (isPremium) {
            OperationTypeSelector(
                selectedType = state.type,
                onTypeChanged = { onIntent(OperationIntent.UpdateType(it)) }
            )
        }

        Spacer(modifier = Modifier.height(AppTheme.dimens.small))

        // --- Form Actions ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                AppTextButton(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = CommonLabels.ACTION_CANCEL)
                }
            }

            Spacer(modifier = Modifier.width(AppTheme.dimens.medium))

            Box(modifier = Modifier.weight(1f)) {
                AppButton(
                    onClick = onSave,
                    enabled = state.isFormValid && !dateManager.isError,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = CommonLabels.ACTION_SAVE,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(AppTheme.dimens.large))
    }
}