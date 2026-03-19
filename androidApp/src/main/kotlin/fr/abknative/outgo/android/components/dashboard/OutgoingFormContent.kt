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
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.android.ui.FormLabels
import fr.abknative.outgo.android.ui.states.OutgoingFormEvent
import fr.abknative.outgo.android.ui.states.OutgoingFormState
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor

@Composable
fun OutgoingFormContent(
    modifier: Modifier = Modifier,
    state: OutgoingFormState,
    onEvent: (OutgoingFormEvent) -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {

    val lockSheetConnection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset = available
        }
    }

    val isEditMode = state.outgoingId != null

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = AppTheme.colors.primary.toColor(),
        unfocusedBorderColor = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.2f),
        focusedLabelColor = AppTheme.colors.primary.toColor(),
        unfocusedLabelColor = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.6f),
        cursorColor = AppTheme.colors.primary.toColor(),
        focusedTextColor = AppTheme.colors.textPrimary.toColor(),
        unfocusedTextColor = AppTheme.colors.textPrimary.toColor()
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(AppTheme.spacing.large)
            .nestedScroll(lockSheetConnection)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Titre ---
        Text(
            text = if (isEditMode) FormLabels.SHEET_TITLE_EDIT else FormLabels.SHEET_TITLE_ADD,
            style = AppTheme.typo.subtitle,
            color = AppTheme.colors.textPrimary.toColor()
        )

        Spacer(modifier = Modifier.height(AppTheme.spacing.small))

        // --- Champ : Nom de la dépense ---
        OutlinedTextField(
            value = state.nameBuffer,
            onValueChange = { onEvent(OutgoingFormEvent.UpdateName(it)) },
            label = { Text(text = FormLabels.FIELD_NAME, style = AppTheme.typo.caption) },
            placeholder = { Text(text = FormLabels.FIELD_PLACE_HOLDER_NAME, style = AppTheme.typo.body) },
            singleLine = true,
            textStyle = AppTheme.typo.body,
            colors = textFieldColors,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // --- Champ : Montant ---
        OutlinedTextField(
            value = state.amountBuffer,
            onValueChange = { onEvent(OutgoingFormEvent.UpdateAmount(it)) },
            label = { Text(text = FormLabels.FIELD_AMOUNT, style = AppTheme.typo.caption) },
            placeholder = { Text(text = FormLabels.FIELD_PLACE_HOLDER_AMOUNT, style = AppTheme.typo.body) },
            singleLine = true,
            textStyle = AppTheme.typo.body,
            colors = textFieldColors,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            ),
            suffix = { Text(CommonLabels.CURRENCY_SYMBOL, style = AppTheme.typo.body) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(AppTheme.spacing.medium))

        Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.small)) {

            Text(
                text = FormLabels.FIELD_DATE_DESC,
                style = AppTheme.typo.caption,
                color = AppTheme.colors.textSecondary.toColor(),
                modifier = Modifier.padding(horizontal = AppTheme.spacing.small)
            )

            // --- Nouveau Sélecteur : Date et Récurrence unifiées ---
            OutgoingDateSelector(
                selectedDay = state.dueDayBuffer,
                selectedMonth = state.dueMonthBuffer,
                onDayChanged = { onEvent(OutgoingFormEvent.UpdateDueDay(it)) },
                onMonthChanged = { onEvent(OutgoingFormEvent.UpdateDueMonth(it)) }
            )
        }

        Spacer(modifier = Modifier.height(AppTheme.spacing.medium))

        // --- Actions Boutons ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = AppTheme.spacing.medium),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onCancel,
                modifier = Modifier.padding(end = AppTheme.spacing.medium),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = AppTheme.colors.textSecondary.toColor()
                )
            ) {
                Text(text = CommonLabels.ACTION_CANCEL, style = AppTheme.typo.label)
            }

            Button(
                onClick = onSave,
                enabled = state.isValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.colors.primary.toColor(),
                    contentColor = AppTheme.colors.textOnBrand.toColor(),
                    disabledContainerColor = AppTheme.colors.surface100.toColor().copy(alpha = 0.5f),
                    disabledContentColor = AppTheme.colors.textSecondary.toColor()
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