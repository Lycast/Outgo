package fr.abknative.outgo.android.core.components.inputs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.VisualTransformation
import fr.abknative.outgo.android.core.designsystem.AppText
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    shape: Shape = AppTheme.shapes.medium
) {

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = AppTheme.colors.surface50.toColor().copy(alpha = 0.2f),
        focusedContainerColor = AppTheme.colors.surface50.toColor().copy(alpha = 0.3f),
        focusedBorderColor = AppTheme.colors.primary.toColor(),
        unfocusedBorderColor = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.2f),
        focusedLabelColor = AppTheme.colors.primary.toColor(),
        unfocusedLabelColor = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.6f),
        cursorColor = AppTheme.colors.primary.toColor(),
        focusedTextColor = AppTheme.colors.textPrimary.toColor(),
        unfocusedTextColor = AppTheme.colors.textPrimary.toColor(),
        errorBorderColor = AppTheme.colors.error.toColor(),
        errorLabelColor = AppTheme.colors.error.toColor(),
        errorCursorColor = AppTheme.colors.error.toColor()
    )

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { AppText(text = label, style = AppTheme.typo.caption) },
        placeholder = { AppText(text = placeholder) },
        isError = isError,
        enabled = enabled,
        singleLine = singleLine,
        textStyle = AppTheme.typo.body,
        colors = textFieldColors,
        keyboardOptions = keyboardOptions,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        suffix = suffix,
        visualTransformation = visualTransformation,
        shape = shape,
        modifier = modifier.fillMaxWidth()
    )
}