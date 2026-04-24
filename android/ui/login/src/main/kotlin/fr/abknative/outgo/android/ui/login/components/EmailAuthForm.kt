package fr.abknative.outgo.android.ui.login.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.core.LoginLabels
import fr.abknative.outgo.android.core.components.buttons.AppButton
import fr.abknative.outgo.android.core.components.buttons.AppTextButton
import fr.abknative.outgo.android.core.components.inputs.AppTextField
import fr.abknative.outgo.android.core.designsystem.AppText
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor

@Composable
fun EmailAuthForm(
    modifier: Modifier = Modifier,
    emailInput: String,
    passwordInput: String,
    isFormValid: Boolean,
    isLoading: Boolean,
    isLoginMode: Boolean,
    submitBtnLabel: String? = null,
    showToggleMode: Boolean = true,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onToggleMode: () -> Unit
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        AppTextField(
            value = emailInput,
            onValueChange = onEmailChange,
            label = LoginLabels.EMAIL_LABEL,
            placeholder = "",
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.small))

        AppTextField(
            value = passwordInput,
            onValueChange = onPasswordChange,
            label = LoginLabels.PASSWORD_LABEL,
            placeholder = "",
            visualTransformation = PasswordVisualTransformation(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.extraLarge))

        AppButton(
            onClick = onSubmit,
            enabled = isFormValid && !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp,
                    color = AppTheme.colors.textOnBrand.toColor()
                )
            } else {
                AppText(
                    text = submitBtnLabel ?: if (isLoginMode) LoginLabels.SUBMIT_BUTTON else LoginLabels.REGISTER_BUTTON,
                    color = AppTheme.colors.textOnBrand.toColor()
                )
            }
        }

        if (showToggleMode) {
            Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

            AppTextButton(
                onClick = onToggleMode,
                enabled = !isLoading
            ) {
                AppText(
                    text = if (isLoginMode) LoginLabels.SUBMIT_ACTION else LoginLabels.REGISTER_ACTION,
                    color = AppTheme.colors.textSecondary.toColor()
                )
            }
        }
    }
}