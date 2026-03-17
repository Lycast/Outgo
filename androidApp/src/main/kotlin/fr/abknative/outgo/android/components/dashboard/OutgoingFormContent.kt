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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.android.ui.FormLabels
import fr.abknative.outgo.android.ui.states.OutgoingFormEvent
import fr.abknative.outgo.android.ui.states.OutgoingFormState
import fr.abknative.outgo.android.ui.theme.AppTheme

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
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                return available
            }
        }
    }

    val isEditMode = state.outgoingId != null

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
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(AppTheme.spacing.small))

        // --- Champ : Nom de la dépense ---
        OutlinedTextField(
            value = state.nameBuffer,
            onValueChange = { onEvent(OutgoingFormEvent.UpdateName(it)) },
            label = { Text(FormLabels.FIELD_NAME) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // --- Champ : Montant ---
        OutlinedTextField(
            value = state.amountBuffer,
            onValueChange = { newValue ->
                onEvent(OutgoingFormEvent.UpdateAmount(newValue))
            },
            label = { Text(FormLabels.FIELD_AMOUNT) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            ),
            suffix = { Text(CommonLabels.CURRENCY_SYMBOL) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(AppTheme.spacing.small))

        Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.small)) {

            Text(
                text = FormLabels.FIELD_DATE_DESC,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                modifier = Modifier.padding(end = AppTheme.spacing.medium)
            ) {
                Text(CommonLabels.ACTION_CANCEL)
            }

            Button(
                onClick = onSave,
                enabled = state.isValid
            ) {
                Text(CommonLabels.ACTION_SAVE, color = Color.White)
            }
        }
    }
}