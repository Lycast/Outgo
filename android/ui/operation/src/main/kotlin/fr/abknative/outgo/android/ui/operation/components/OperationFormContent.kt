package fr.abknative.outgo.android.ui.operation.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.core.CommonLabels
import fr.abknative.outgo.android.core.components.buttons.AppButton
import fr.abknative.outgo.android.core.components.buttons.AppTextButton
import fr.abknative.outgo.android.core.components.feedback.FormattedDateInput
import fr.abknative.outgo.android.core.components.inputs.AppTextField
import fr.abknative.outgo.android.core.components.layout.CardSplitSkeleton
import fr.abknative.outgo.android.core.designsystem.AppText
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor
import fr.abknative.outgo.android.ui.operation.OperationLabels
import fr.abknative.outgo.operation.api.OperationIntent
import fr.abknative.outgo.operation.api.OperationState
import fr.abknative.outgo.wallet.api.model.operation.Recurrence

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
        AppText(
            text = if (isEditMode) OperationLabels.SHEET_TITLE_EDIT else OperationLabels.SHEET_TITLE_ADD,
            style = AppTheme.typo.subtitle,
            modifier = Modifier.padding(top = AppTheme.dimens.medium)
        )

        // --- Field: Name ---
        AppTextField(
            value = state.name,
            onValueChange = { onIntent(OperationIntent.UpdateName(it)) },
            label = OperationLabels.FIELD_NAME,
            placeholder = OperationLabels.FIELD_PLACE_HOLDER_NAME,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            )
        )

        // --- Field: Amount ---
        AppTextField(
            value = state.amount,
            onValueChange = { onIntent(OperationIntent.UpdateAmount(it)) },
            label = OperationLabels.FIELD_AMOUNT,
            placeholder = OperationLabels.FIELD_PLACE_HOLDER_AMOUNT,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
            suffix = {
                AppText(
                    text = CommonLabels.CURRENCY_SYMBOL,
                    color = AppTheme.colors.textSecondary.toColor()
                )
            }
        )

        CardSplitSkeleton(
            leftContent = {
                // --- Field: StartDate ---
                FormattedDateInput(
                    value = state.startDateInputBuffer,
                    onValueChange = { onIntent(OperationIntent.UpdateStartDateInput(it)) },
                    onDateSelected = { onIntent(OperationIntent.SelectStartDateFromPicker(it)) },
                    initialDateMillis = state.startDate,
                    label = OperationLabels.FIELD_START_DATE_LABEL,
                    isError = state.isStartDateError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = if (isPremium) ImeAction.Next else ImeAction.Done
                    )
                )
            },
            rightContent = {
                // --- Field: EndDate ---
                if (state.recurrence != Recurrence.UNIQUE) {
                    Box {
                        FormattedDateInput(
                            value = state.endDateInputBuffer,
                            onValueChange = { onIntent(OperationIntent.UpdateEndDateInput(it)) },
                            onDateSelected = { onIntent(OperationIntent.SelectEndDateFromPicker(it)) },
                            initialDateMillis = state.endDate,
                            label = OperationLabels.FIELD_END_DATE_LABEL,
                            isError = state.isEndDateError,
                        )
                        if (state.endDateInputBuffer.isNotEmpty()) {
                            Box(Modifier
                                .offset(y = (-2).dp)
                                .padding(start = AppTheme.dimens.medium)
                                .border(1.dp, AppTheme.colors.textSecondary.toColor().copy(0.2f) ,shape = AppTheme.shapes.small)
                                .clip(RoundedCornerShape(AppTheme.dimens.small))
                                .background(AppTheme.colors.surface200.toColor())
                            ) {
                                AppText(
                                    text = CommonLabels.ACTION_CLEAR,
                                    style = AppTheme.typo.label,
                                    color = AppTheme.colors.error.toColor().copy(alpha = 0.4f),
                                    modifier = Modifier
                                        .padding(horizontal = AppTheme.dimens.extraLarge, vertical = AppTheme.dimens.extraSmall)
                                        .clickable { onIntent(OperationIntent.ClearEndDate) }
                                )
                            }
                        }
                    }
                }
            }
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
                    AppText(text = CommonLabels.ACTION_CANCEL)
                }
            }

            Spacer(modifier = Modifier.width(AppTheme.dimens.medium))

            Box(modifier = Modifier.weight(1f)) {
                AppButton(
                    onClick = onSave,
                    enabled = state.isFormValid,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AppText(
                        text = CommonLabels.ACTION_SAVE,
                        color = AppTheme.colors.textOnBrand.toColor()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(AppTheme.dimens.large))
    }
}