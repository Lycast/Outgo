package fr.abknative.outgo.android.components.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.components.buttons.AppButton
import fr.abknative.outgo.android.designsystem.components.buttons.AppTextButton
import fr.abknative.outgo.android.designsystem.components.inputs.AppTextField
import fr.abknative.outgo.android.designsystem.foundation.AppText
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.LoginLabels

@Composable
fun EmailAuthForm(
    emailInput: String,
    passwordInput: String,
    isFormValid: Boolean,
    isLoading: Boolean,
    isLoginMode: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onToggleMode: () -> Unit,
    modifier: Modifier = Modifier
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
                    strokeWidth = 2.dp
                )
            } else {
                AppText(
                    text = if (isLoginMode) LoginLabels.SUBMIT_BUTTON else LoginLabels.REGISTER_BUTTON,
                    color = AppTheme.colors.textOnBrand.toColor()
                )
            }
        }

        Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

        // Le texte cliquable pour basculer de mode
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