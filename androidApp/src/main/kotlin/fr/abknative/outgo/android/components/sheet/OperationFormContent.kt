package fr.abknative.outgo.android.components.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
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
import fr.abknative.outgo.android.components.common.AppTextField
import fr.abknative.outgo.android.components.common.FormattedDateInput
import fr.abknative.outgo.android.components.sheet.OperationTypeSelector
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.android.ui.FormLabels
import fr.abknative.outgo.android.ui.states.OperationFormEvent
import fr.abknative.outgo.android.ui.states.OperationFormState
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperationFormContent(
    modifier: Modifier = Modifier,
    state: OperationFormState,
    isPremium: Boolean,
    onEvent: (OperationFormEvent) -> Unit,
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
            .padding(horizontal = AppTheme.spacing.large)
            .nestedScroll(lockSheetConnection)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
// --- Titre ---
        Text(
            text = if (isEditMode) FormLabels.SHEET_TITLE_EDIT else FormLabels.SHEET_TITLE_ADD,
            style = AppTheme.typo.subtitle,
            color = AppTheme.colors.textPrimary.toColor()
        )

// --- Champ : Nom de la dépense/revenu ---
        AppTextField(
            value = state.nameBuffer,
            onValueChange = { onEvent(OperationFormEvent.UpdateName(it)) },
            label = FormLabels.FIELD_NAME,
            placeholder = FormLabels.FIELD_PLACE_HOLDER_NAME,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

// --- Champ : Montant ---
        AppTextField(
            value = state.amountBuffer,
            onValueChange = { onEvent(OperationFormEvent.UpdateAmount(it)) },
            label = FormLabels.FIELD_AMOUNT,
            placeholder = FormLabels.FIELD_PLACE_HOLDER_AMOUNT,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
            suffix = { Text(CommonLabels.CURRENCY_SYMBOL, style = AppTheme.typo.body) },
            modifier = Modifier.fillMaxWidth()
        )

// --- Sélecteur de start date ---
        FormattedDateInput(
            value = state.dateBuffer,
            onValueChange = { onEvent(OperationFormEvent.UpdateDateString(it)) },
            onDateSelected = { millis -> onEvent(OperationFormEvent.UpdateDateMillis(millis)) },
            initialDateMillis = state.startDate,
            label = "Date de l'opération",
            isError = state.isDateError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = if (isPremium) ImeAction.Next else ImeAction.Done
            )
        )

// --- Sélecteur de Récurrence ---
        RecurrenceSelector(
            selectedRecurrence = state.recurrenceSelection,
            onRecurrenceChanged = { onEvent(OperationFormEvent.UpdateRecurrence(it)) }
        )

// --- Sélecteur : Type d'opération (Revenu / Dépense) ---
        if (isPremium) {
            OperationTypeSelector(
                selectedType = state.typeSelection,
                onTypeChanged = { onEvent(OperationFormEvent.UpdateType(it)) }
            )
        }

        Spacer(modifier = Modifier.height(AppTheme.spacing.small))


// --- Actions Boutons ---
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Cancel button
            TextButton(
                onClick = onCancel,
                modifier = Modifier.padding(end = AppTheme.spacing.small),
                colors = ButtonDefaults.textButtonColors(contentColor = AppTheme.colors.textSecondary.toColor())
            ) {
                Text(
                    text = CommonLabels.ACTION_CANCEL, style = AppTheme.typo.label,
                    modifier = Modifier.padding(end = AppTheme.spacing.medium)
                )
            }

            // Save button
            Button(
                onClick = onSave,
                enabled = state.isValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.colors.primary.toColor(),
                    contentColor = AppTheme.colors.textOnBrand.toColor(),
                    disabledContainerColor = AppTheme.colors.surface50.toColor().copy(alpha = 0.5f),
                    disabledContentColor = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.5f)
                )
            ) {
                Text(
                    text = CommonLabels.ACTION_SAVE,
                    style = AppTheme.typo.label,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}