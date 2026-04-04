package fr.abknative.outgo.android.components.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.android.ui.FormLabels
import fr.abknative.outgo.android.ui.WalletEditDialogLabels
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.OutgoTheme
import fr.abknative.outgo.android.ui.theme.toColor

@Composable
fun WalletEditDialog(
    initialWalletName: String,
    currentIncomeInCents: Long,
    onDismiss: () -> Unit,
    onConfirm: (newName: String, newIncomeInCents: Long) -> Unit
) {

    var nameValue by remember { mutableStateOf(initialWalletName) }

    var amountValue by remember {
        mutableStateOf(
            if (currentIncomeInCents > 0) {
                currentIncomeInCents.toBigDecimal()
                    .movePointLeft(2)
                    .toPlainString()
            } else ""
        )
    }

    val parsedAmount = remember(amountValue) {
        amountValue.toBigDecimalOrNull()?.movePointRight(2)?.toLong()
    }

    val isValidInput = parsedAmount != null && amountValue.isNotBlank() && nameValue.isNotBlank()

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = AppTheme.colors.primary.toColor(),
        unfocusedBorderColor = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.3f),
        focusedLabelColor = AppTheme.colors.primary.toColor(),
        unfocusedLabelColor = AppTheme.colors.textSecondary.toColor(),
        cursorColor = AppTheme.colors.primary.toColor(),
        focusedTextColor = AppTheme.colors.textPrimary.toColor(),
        unfocusedTextColor = AppTheme.colors.textPrimary.toColor(),
        focusedPlaceholderColor = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.5f),
        unfocusedPlaceholderColor = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.5f),
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AppTheme.colors.surface200.toColor(),
        title = {
            Text(
                text = WalletEditDialogLabels.DIALOG_BUDGET_TITLE, // Tu pourras renommer ce label plus tard
                style = AppTheme.typo.subtitle,
                color = AppTheme.colors.textPrimary.toColor()
            )
        },
        text = {
            Column {
                Text(
                    text = WalletEditDialogLabels.DIALOG_BUDGET_DESC,
                    style = AppTheme.typo.body,
                    color = AppTheme.colors.textSecondary.toColor()
                )

                Spacer(modifier = Modifier.height(AppTheme.spacing.large))

                // --- Champ : Nom du compte ---
                OutlinedTextField(
                    value = nameValue,
                    onValueChange = { nameValue = it },
                    label = {
                        Text(
                            text = FormLabels.FIELD_NAME, // "Nom"
                            style = AppTheme.typo.caption
                        )
                    },
                    placeholder = {
                        Text("Mon Compte Principal", style = AppTheme.typo.body)
                    },
                    textStyle = AppTheme.typo.body.copy(color = AppTheme.colors.textPrimary.toColor()),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )

                Spacer(modifier = Modifier.height(AppTheme.spacing.medium))

                // --- Champ : Montant du budget ---
                OutlinedTextField(
                    value = amountValue,
                    onValueChange = { newValue ->
                        if (newValue.length <= 15) {
                            val sanitizedValue = newValue.replace(',', '.')
                            if (sanitizedValue.isEmpty()
                                || sanitizedValue.count { it == '.' } <= 1
                                && sanitizedValue.all { it.isDigit() || it == '.' }
                            ) {
                                amountValue = sanitizedValue
                            }
                        }
                    },
                    label = {
                        Text(
                            text = WalletEditDialogLabels.DIALOG_BUDGET_FIELD,
                            style = AppTheme.typo.caption
                        )
                    },
                    placeholder = {
                        Text(FormLabels.FIELD_PLACE_HOLDER_AMOUNT, style = AppTheme.typo.body)
                    },
                    suffix = {
                        Text(CommonLabels.CURRENCY_SYMBOL, style = AppTheme.typo.body)
                    },
                    textStyle = AppTheme.typo.body.copy(color = AppTheme.colors.textPrimary.toColor()),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (isValidInput) onConfirm(nameValue, parsedAmount)
                        }
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )

                Spacer(modifier = Modifier.height(AppTheme.spacing.large))

                Text(
                    text = WalletEditDialogLabels.DIALOG_BUDGET_INFO,
                    style = AppTheme.typo.caption,
                    color = AppTheme.colors.textSecondary.toColor()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (isValidInput) onConfirm(nameValue, parsedAmount) },
                enabled = isValidInput,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.colors.primary.toColor(),
                    contentColor = AppTheme.colors.textOnBrand.toColor(),
                    disabledContainerColor = AppTheme.colors.surface100.toColor(),
                    disabledContentColor = AppTheme.colors.textSecondary.toColor()
                )
            ) {
                Text(
                    text = CommonLabels.ACTION_SAVE,
                    style = AppTheme.typo.label
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = AppTheme.colors.textSecondary.toColor()
                )
            ) {
                Text(
                    text = CommonLabels.ACTION_CANCEL,
                    style = AppTheme.typo.label
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewBudgetEditDialog_LargeAmount() {
    OutgoTheme {
        WalletEditDialog(
            initialWalletName = "Compte Courant",
            currentIncomeInCents = 3549000000000L, // 35 000 000 000 d'euros
            onDismiss = {},
            onConfirm = { _, _ -> }
        )
    }
}