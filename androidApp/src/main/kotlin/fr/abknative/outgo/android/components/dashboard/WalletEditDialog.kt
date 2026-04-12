package fr.abknative.outgo.android.components.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import fr.abknative.outgo.android.designsystem.components.buttons.AppButton
import fr.abknative.outgo.android.designsystem.components.buttons.AppTextButton
import fr.abknative.outgo.android.designsystem.components.cards.GlassCard
import fr.abknative.outgo.android.designsystem.components.inputs.AppTextField
import fr.abknative.outgo.android.designsystem.foundation.AppBackground
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.OutgoTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.android.ui.DialogLabels
import fr.abknative.outgo.android.ui.FormLabels

/**
 * A dialog allowing the user to edit their wallet's name and monthly income.
 * Styled with [GlassCard] to maintain the application's visual identity.
 */
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

    Dialog(onDismissRequest = onDismiss) {
        GlassCard {
            Column(
                modifier = Modifier.padding(AppTheme.dimens.large),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- Title ---
                Text(
                    text = DialogLabels.DIALOG_BUDGET_TITLE,
                    style = AppTheme.typo.subtitle,
                    color = AppTheme.colors.primary.toColor(),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

                Text(
                    text = DialogLabels.DIALOG_BUDGET_DESC,
                    style = AppTheme.typo.body,
                    color = AppTheme.colors.textSecondary.toColor()
                )

                Spacer(modifier = Modifier.height(AppTheme.dimens.large))

                // --- Field: Wallet Name ---
                AppTextField(
                    value = nameValue,
                    onValueChange = { nameValue = it },
                    label = FormLabels.FIELD_NAME,
                    placeholder = "Mon Compte Principal",
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

                // --- Field: Budget Amount ---
                AppTextField(
                    value = amountValue,
                    onValueChange = { newValue ->
                        val sanitized = newValue.replace(',', '.')
                        if (sanitized.length <= 10 &&
                            (sanitized.isEmpty() || (sanitized.count { it == '.' } <= 1 && sanitized.all { it.isDigit() || it == '.' }))) {
                            amountValue = sanitized
                        }
                    },
                    label = DialogLabels.DIALOG_BUDGET_FIELD,
                    placeholder = FormLabels.FIELD_PLACE_HOLDER_AMOUNT,
                    suffix = {
                        Text(CommonLabels.CURRENCY_SYMBOL, style = AppTheme.typo.body)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    )
                )

                Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

                // --- Info Hint ---
                Text(
                    text = DialogLabels.DIALOG_BUDGET_INFO,
                    style = AppTheme.typo.caption,
                    color = AppTheme.colors.textSecondary.toColor(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(AppTheme.dimens.extraLarge))

                // --- Action Buttons ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        AppTextButton(
                            onClick = onDismiss,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = CommonLabels.ACTION_CANCEL)
                        }
                    }

                    Spacer(modifier = Modifier.width(AppTheme.dimens.medium))

                    Box(modifier = Modifier.weight(1f)) {
                        AppButton(
                            onClick = { if (isValidInput) onConfirm(nameValue, parsedAmount) },
                            enabled = isValidInput,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = CommonLabels.ACTION_SAVE)
                        }
                    }
                }
            }
        }
    }
}

// --- PREVIEWS ---

@Preview(showBackground = true)
@Composable
fun PreviewBudgetEditDialog_Standard() {
    OutgoTheme {
        AppBackground {
            WalletEditDialog(
                initialWalletName = "Compte Courant",
                currentIncomeInCents = 150000L,
                onDismiss = {},
                onConfirm = { _, _ -> }
            )
        }
    }
}