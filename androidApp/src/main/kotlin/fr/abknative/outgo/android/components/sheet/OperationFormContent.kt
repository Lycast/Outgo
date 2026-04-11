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
import fr.abknative.outgo.android.ui.states.OperationFormEvent
import fr.abknative.outgo.android.ui.states.OperationFormState

/**
 * Main content for the Operation Form Sheet.
 * Orchestrates the various Design System inputs to provide a consistent data entry experience.
 *
 * @param state The current UI state of the form.
 * @param isPremium Whether the user has access to premium fields (like Type Selection).
 * @param onEvent Callback to propagate user interactions back to the presenter.
 * @param onCancel Callback invoked when the user wants to discard changes.
 * @param onSave Callback invoked when the user submits the form.
 */
@Composable
fun OperationFormContent(
    modifier: Modifier = Modifier,
    state: OperationFormState,
    isPremium: Boolean,
    onEvent: (OperationFormEvent) -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit,
) {
    // Prevents the parent BottomSheet from intercepting scrolls when the form is long
    val lockSheetConnection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset =
                available
        }
    }

    val isEditMode = state.operationId != null

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
            value = state.nameBuffer,
            onValueChange = { onEvent(OperationFormEvent.UpdateName(it)) },
            label = FormLabels.FIELD_NAME,
            placeholder = FormLabels.FIELD_PLACE_HOLDER_NAME,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            )
        )

        // --- Field: Amount ---
        AppTextField(
            value = state.amountBuffer,
            onValueChange = { onEvent(OperationFormEvent.UpdateAmount(it)) },
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
            value = state.dateBuffer,
            onValueChange = { onEvent(OperationFormEvent.UpdateDateString(it)) },
            onDateSelected = { millis -> onEvent(OperationFormEvent.UpdateDateMillis(millis)) },
            initialDateMillis = state.startDate,
            label = FormLabels.FIELD_DATE_LABEL,
            isError = state.isDateError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = if (isPremium) ImeAction.Next else ImeAction.Done
            )
        )

        // --- Field: Recurrence ---
        RecurrenceSelector(
            selectedRecurrence = state.recurrenceSelection,
            onRecurrenceChanged = { onEvent(OperationFormEvent.UpdateRecurrence(it)) }
        )

        // --- Field: Type (Premium Only) ---
        if (isPremium) {
            OperationTypeSelector(
                selectedType = state.typeSelection,
                onTypeChanged = { onEvent(OperationFormEvent.UpdateType(it)) }
            )
        }

        Spacer(modifier = Modifier.height(AppTheme.dimens.small))

        // --- Form Actions ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cancel Action
            Box(modifier = Modifier.weight(1f)) {
                AppTextButton(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = CommonLabels.ACTION_CANCEL)
                }
            }

            Spacer(modifier = Modifier.width(AppTheme.dimens.medium))

            // Save Action
            Box(modifier = Modifier.weight(1f)) {
                AppButton(
                    onClick = onSave,
                    enabled = state.isValid,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = CommonLabels.ACTION_SAVE,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Safety spacer for bottom navigation bars or keyboards
        Spacer(modifier = Modifier.height(AppTheme.dimens.large))
    }
}