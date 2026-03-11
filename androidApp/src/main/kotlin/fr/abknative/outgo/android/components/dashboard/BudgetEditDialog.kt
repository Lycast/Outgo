package fr.abknative.outgo.android.components.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.ui.BudgetEditDialogLabels
import fr.abknative.outgo.android.ui.CommonLabels

@Composable
fun BudgetEditDialog(
    currentIncomeInCents: Long,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit
) {

    var textValue by remember {
        mutableStateOf(if (currentIncomeInCents > 0) (currentIncomeInCents / 100.0).toString() else "")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = BudgetEditDialogLabels.DIALOG_BUDGET_TITLE,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column {
                Text(
                    text = BudgetEditDialogLabels.DIALOG_BUDGET_DESC,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = textValue,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() || it == '.' || it == ',' }) {
                            textValue = newValue.replace(',', '.')
                        }
                    },
                    label = { Text(BudgetEditDialogLabels.DIALOG_BUDGET_FIELD) },
                    placeholder = { Text("0.00") },
                    suffix = { Text(CommonLabels.CURRENCY_SYMBOL) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amountInCents = textValue.toBigDecimalOrNull()
                        ?.movePointRight(2)
                        ?.toLong() ?: 0L
                    onConfirm(amountInCents)
                },
                enabled = textValue.isNotBlank()
            ) {
                Text(text = CommonLabels.ACTION_SAVE)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = CommonLabels.ACTION_CANCEL)
            }
        }
    )
}