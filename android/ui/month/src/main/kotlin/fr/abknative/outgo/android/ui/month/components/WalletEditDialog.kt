package fr.abknative.outgo.android.ui.month.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import fr.abknative.outgo.android.core.CommonLabels
import fr.abknative.outgo.android.core.components.buttons.AppButton
import fr.abknative.outgo.android.core.components.buttons.AppTextButton
import fr.abknative.outgo.android.core.components.cards.GlassCard
import fr.abknative.outgo.android.core.components.inputs.AppTextField
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor
import fr.abknative.outgo.android.ui.month.MonthLabels

@Composable
fun WalletEditDialog(
    nameBuffer: String,
    amountBuffer: String,
    isValid: Boolean,
    onNameChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {

    Dialog(onDismissRequest = onDismiss) {
        GlassCard {
            Column(
                modifier = Modifier.padding(AppTheme.dimens.large),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- Title ---
                Text(
                    text = MonthLabels.BUDGET_TITLE,
                    style = AppTheme.typo.subtitle,
                    color = AppTheme.colors.primary.toColor(),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

                Text(
                    text = MonthLabels.BUDGET_DESC,
                    style = AppTheme.typo.body,
                    color = AppTheme.colors.textSecondary.toColor()
                )

                Spacer(modifier = Modifier.height(AppTheme.dimens.large))

                // --- Field: Wallet Name ---
                AppTextField(
                    value = nameBuffer,
                    onValueChange = onNameChange,
                    label = MonthLabels.WALLET_NAME_LABEL,
                    placeholder = MonthLabels.WALLET_NAME_PLACEHOLDER,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

                // --- Field: Budget Amount ---
                AppTextField(
                    value = amountBuffer,
                    onValueChange = onAmountChange,
                    label = MonthLabels.BUDGET_FIELD,
                    placeholder = MonthLabels.AMOUNT_PLACEHOLDER,
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
                    text = MonthLabels.BUDGET_INFO,
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
                            onClick = onConfirm,
                            enabled = isValid,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = CommonLabels.ACTION_SAVE, color = AppTheme.colors.textOnBrand.toColor())
                        }
                    }
                }
            }
        }
    }
}