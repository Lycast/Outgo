package fr.abknative.outgo.android.components.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.designsystem.components.buttons.AppButton
import fr.abknative.outgo.android.designsystem.components.inputs.AppTextField
import fr.abknative.outgo.android.designsystem.foundation.AppText
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.android.ui.OnboardingLabels

@Composable
fun ConfigurationStep(
    walletName: String,
    incomeAmountText: String,
    isLoading: Boolean,
    errorMessage: String?,
    onNameChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBackClicked: () -> Unit
) {

    Box(Modifier.fillMaxSize()) {

        Row(modifier = Modifier
            .align(Alignment.TopStart)
            .padding(top = AppTheme.dimens.extraSmall, start = AppTheme.dimens.medium),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClicked
            ) {
                Icon(
                    painter = painterResource(R.drawable.caret_left),
                    contentDescription = CommonLabels.ACTION_BACK,
                    tint = AppTheme.colors.textPrimary.toColor()
                )
            }
            AppText(
                text = CommonLabels.ACTION_BACK
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(AppTheme.dimens.extraLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Icon(
                painter = painterResource(R.drawable.bank_duotone),
                contentDescription = CommonLabels.ACTION_BACK,
                tint = AppTheme.colors.secondary.toColor(),
                modifier = Modifier.size(AppTheme.dimens.big),
            )

            Spacer(Modifier.height(AppTheme.dimens.medium))

            // --- Title ---
            AppText(
                text = OnboardingLabels.CONFIG_TITLE,
                style = AppTheme.typo.title,
                color = AppTheme.colors.primary.toColor()
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
                AppText(
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
                    AppText(
                        text = OnboardingLabels.SUBMIT_ONBOARDING,
                        color = AppTheme.colors.textOnBrand.toColor()
                    )
                }
            }
        }
    }
}