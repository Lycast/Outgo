package fr.abknative.outgo.android.components.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.android.ui.FormLabels
import fr.abknative.outgo.android.ui.states.OutgoingFormEvent
import fr.abknative.outgo.android.ui.states.OutgoingFormState
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.outgoing.api.Recurrence

@Composable
fun OutgoingFormContent(
    modifier: Modifier = Modifier,
    state: OutgoingFormState,
    onEvent: (OutgoingFormEvent) -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    val isEditMode = state.outgoingId != null

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(AppTheme.spacing.large),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Titre ---
        Text(
            text = if (isEditMode) FormLabels.SHEET_TITLE_EDIT else FormLabels.SHEET_TITLE_ADD,
            style = MaterialTheme.typography.titleLarge
        )

        // --- Champ : Nom de la dépense ---
        OutlinedTextField(
            value = state.nameBuffer,
            onValueChange = { onEvent(OutgoingFormEvent.UpdateName(it)) },
            label = { Text(FormLabels.FIELD_NAME) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        // --- Sélecteur : Récurrence ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.medium)
        ) {
            val chipColors = FilterChipDefaults.filterChipColors(
                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                labelColor = MaterialTheme.colorScheme.onSurface,
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = Color.White,
            )

            FilterChip(
                selected = state.recurrenceSelection == Recurrence.MONTHLY,
                onClick = { onEvent(OutgoingFormEvent.UpdateRecurrence(Recurrence.MONTHLY)) },
                label = { Text(FormLabels.CYCLE_MONTHLY) },
                colors = chipColors,
                border = null,
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                selected = state.recurrenceSelection == Recurrence.YEARLY,
                onClick = { onEvent(OutgoingFormEvent.UpdateRecurrence(Recurrence.YEARLY)) },
                label = { Text(FormLabels.CYCLE_YEARLY) },
                colors = chipColors,
                border = null,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.large)
        ) {
            // --- Champ : Montant ---
            OutlinedTextField(
                value = state.amountBuffer,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() || it == '.' || it == ',' }) {
                        onEvent(OutgoingFormEvent.UpdateAmount(newValue))
                    }
                },
                label = { Text(FormLabels.FIELD_AMOUNT) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next),
                suffix = { Text(CommonLabels.CURRENCY_SYMBOL) },
                modifier = Modifier.weight(1f).fillMaxHeight()
            )

            // --- Champ : Jour de prélèvement ---
            OutlinedTextField(
                value = state.dueDayBuffer,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) onEvent(OutgoingFormEvent.UpdateDueDay(newValue))
                },
                label = { Text(FormLabels.FIELD_DATE) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number,
                    imeAction = if(state.recurrenceSelection == Recurrence.YEARLY) ImeAction.Next else ImeAction.Done
                ),
                modifier = Modifier.weight(1f).fillMaxHeight()
            )
        }

        // --- Champ conditionnel : Mois de prélèvement ---
        if (state.recurrenceSelection == Recurrence.YEARLY) {
            OutlinedTextField(
                value = state.dueMonthBuffer,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) onEvent(OutgoingFormEvent.UpdateDueMonth(newValue))
                },
                label = { Text(FormLabels.FIELD_MONTH) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // --- Actions Boutons ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onCancel,
                modifier = Modifier.padding(end = AppTheme.spacing.medium)
            ) {
                Text(CommonLabels.ACTION_CANCEL)
            }

            Button(
                onClick = onSave,
                enabled = state.isValid
            ) {
                Text(CommonLabels.ACTION_SAVE)
            }
        }
    }
}