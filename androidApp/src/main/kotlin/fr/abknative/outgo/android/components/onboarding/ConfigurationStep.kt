package fr.abknative.outgo.android.components.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.components.buttons.AppButton
import fr.abknative.outgo.android.designsystem.components.inputs.AppTextField
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.OnboardingLabels

/**
 * Onboarding step for initial wallet configuration.
 * Uses Design System components to ensure brand consistency from the very first screen.
 *
 * @param walletName The current buffer for the wallet name.
 * @param incomeAmountText The current buffer for the initial income/balance.
 * @param isLoading Whether a network or disk operation is in progress.
 * @param errorMessage Optional error message to display above the action button.
 * @param onNameChange Callback for wallet name updates.
 * @param onAmountChange Callback for amount updates.
 * @param onSubmit Final action to move to the next step.
 */
@Composable
fun ConfigurationStep(
    walletName: String,
    incomeAmountText: String,
    isLoading: Boolean,
    errorMessage: String?,
    onNameChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(AppTheme.dimens.extraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // todo add back bar

        // --- Title ---
        Text(
            text = OnboardingLabels.CONFIG_TITLE,
            style = AppTheme.typo.subtitle,
            color = AppTheme.colors.primary.toColor(),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.extraLarge))

        // --- Wallet Name Input ---
        AppTextField(
            value = walletName,
            onValueChange = onNameChange,
            label = OnboardingLabels.WALLET_NAME_LABEL,
            placeholder = OnboardingLabels.WALLET_NAME_PLACEHOLDER,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.large))

        // --- Income Amount Input ---
        AppTextField(
            value = incomeAmountText,
            onValueChange = onAmountChange,
            label = OnboardingLabels.INCOME_AMOUNT_LABEL,
            placeholder = OnboardingLabels.INCOME_AMOUNT_PLACEHOLDER,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            )
        )

        // --- Error Feedback ---
        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(AppTheme.dimens.medium))
            Text(
                text = errorMessage,
                color = AppTheme.colors.error.toColor(),
                style = AppTheme.typo.caption,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(AppTheme.dimens.big))

        // --- Action Button ---
        AppButton(
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading && walletName.isNotBlank() && incomeAmountText.isNotBlank()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = AppTheme.colors.textOnBrand.toColor(),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = OnboardingLabels.SUBMIT_ONBOARDING,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}