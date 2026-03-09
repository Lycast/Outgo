package fr.abknative.outgo.android.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.android.ui.FormLabels
import fr.abknative.outgo.android.ui.states.OutgoingFormEvent
import fr.abknative.outgo.android.ui.states.OutgoingFormState
import fr.abknative.outgo.outgoing.api.Recurrence

@Composable
fun OutgoingFormContent(
    modifier: Modifier = Modifier,
    state: OutgoingFormState,
    onEvent: (OutgoingFormEvent) -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit,
    onDelete: (() -> Unit)? = null,
) {

    val isEditMode = state.outgoingId != null

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
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
            modifier = Modifier.fillMaxWidth()
        )

        // --- Sélecteur : Récurrence ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = state.recurrenceSelection == Recurrence.MONTHLY,
                onClick = { onEvent(OutgoingFormEvent.UpdateRecurrence(Recurrence.MONTHLY)) },
                label = { Text(FormLabels.CYCLE_MONTHLY) },
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                selected = state.recurrenceSelection == Recurrence.YEARLY,
                onClick = { onEvent(OutgoingFormEvent.UpdateRecurrence(Recurrence.YEARLY)) },
                label = { Text(FormLabels.CYCLE_YEARLY) },
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                suffix = { Text(CommonLabels.CURRENCY_SYMBOL) },
                modifier = Modifier.weight(1f)
            )

            // --- Champ : Jour de prélèvement ---
            OutlinedTextField(
                value = state.dueDayBuffer,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) onEvent(OutgoingFormEvent.UpdateDueDay(newValue))
                },
                label = { Text(FormLabels.FIELD_DATE) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword), // NumberPassword évite souvent les virgules
                modifier = Modifier.weight(1f)
            )
        }

        // --- Champ conditionnel : Mois de prélèvement (Uniquement si Annuel) ---
        if (state.recurrenceSelection == Recurrence.YEARLY) {
            OutlinedTextField(
                value = state.dueMonthBuffer,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) onEvent(OutgoingFormEvent.UpdateDueMonth(newValue))
                },
                label = { Text(FormLabels.FIELD_MONTH) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // --- Actions (Boutons) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Bouton Supprimer
            if (isEditMode && onDelete != null) {
                TextButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(CommonLabels.ACTION_DELETE)
                }
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            // Boutons Annuler / Enregistrer
            Row(horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onCancel) {
                    Text(CommonLabels.ACTION_CANCEL)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onSave,
                    enabled = state.isValid
                ) {
                    Text(CommonLabels.ACTION_SAVE)
                }
            }
        }
    }
}