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
import fr.abknative.outgo.outgoing.api.BillingCycle

@Composable
fun OutgoingFormContent(
    state: OutgoingFormState,
    onEvent: (OutgoingFormEvent) -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
    isEditMode: Boolean = false
) {
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- Champ : Montant ---
            OutlinedTextField(
                value = state.amountBuffer,
                onValueChange = { onEvent(OutgoingFormEvent.UpdateAmount(it)) },
                label = { Text(FormLabels.FIELD_AMOUNT) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                suffix = { Text(CommonLabels.CURRENCY_SYMBOL) },
                modifier = Modifier.weight(1f)
            )

            // --- Champ : Jour de prélèvement ---
            OutlinedTextField(
                value = state.billingDayBuffer,
                onValueChange = { onEvent(OutgoingFormEvent.UpdateBillingDay(it)) },
                label = { Text(FormLabels.FIELD_DATE) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }

        // --- Sélecteur : Cycle de facturation ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = state.cycleSelection == BillingCycle.MONTHLY,
                onClick = { onEvent(OutgoingFormEvent.UpdateCycle(BillingCycle.MONTHLY)) },
                label = { Text(FormLabels.CYCLE_MONTHLY) },
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                selected = state.cycleSelection == BillingCycle.YEARLY,
                onClick = { onEvent(OutgoingFormEvent.UpdateCycle(BillingCycle.YEARLY)) },
                label = { Text(FormLabels.CYCLE_YEARLY) },
                modifier = Modifier.weight(1f)
            )
        }

        // --- Actions (Boutons) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
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