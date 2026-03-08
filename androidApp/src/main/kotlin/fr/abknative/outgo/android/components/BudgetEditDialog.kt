package fr.abknative.outgo.android.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.ui.BudgetEditDialogLabels
import fr.abknative.outgo.android.ui.CommonLabels

@Composable
fun BudgetEditDialog(
    initialBudgetInEuros: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {

    var budgetBuffer by remember { mutableStateOf(initialBudgetInEuros) }

    // Validation locale
    val isValid = budgetBuffer.isNotBlank() && budgetBuffer.toDoubleOrNull() != null && budgetBuffer.toDouble() >= 0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(BudgetEditDialogLabels.DIALOG_BUDGET_TITLE) },
        text = {
            Column {
                Text(text = BudgetEditDialogLabels.DIALOG_BUDGET_DESC)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = budgetBuffer,
                    onValueChange = { budgetBuffer = it },
                    label = { Text(BudgetEditDialogLabels.DIALOG_BUDGET_FIELD) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    suffix = { Text(CommonLabels.CURRENCY_SYMBOL) }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(budgetBuffer) },
                enabled = isValid
            ) {
                Text(CommonLabels.ACTION_SAVE)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(CommonLabels.ACTION_CANCEL)
            }
        }
    )
}