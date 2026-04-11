package fr.abknative.outgo.android.designsystem.components.inputs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.VisualTransformation
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor

/**
 * A styled OutlinedTextField that follows the Outgo design language.
 * Centralizes color logic, typography, and error states.
 *
 * @param value The input text to be shown.
 * @param onValueChange The callback that is triggered when the input service updates the text.
 * @param label The label to be displayed inside the text field container.
 * @param placeholder The placeholder to be displayed when the text field is empty.
 * @param modifier The modifier to be applied to this text field.
 * @param isError Indicates if the text field's current value is in error.
 * @param enabled Controls the enabled state of this text field.
 * @param leadingIcon The optional leading icon to be displayed at the beginning of the text field.
 * @param trailingIcon The optional trailing icon to be displayed at the end of the text field.
 * @param suffix The optional suffix to be displayed at the end of the text field.
 * @param visualTransformation The visual transformation filter for the text.
 * @param keyboardOptions Software keyboard options.
 * @param singleLine When true, this text field becomes a single horizontally scrolling line.
 * @param shape The shape of the text field's border and container.
 */
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
        // Colors from your theme
        unfocusedContainerColor = AppTheme.colors.surface50.toColor().copy(alpha = 0.2f),
        focusedContainerColor = AppTheme.colors.surface50.toColor().copy(alpha = 0.3f),

        focusedBorderColor = AppTheme.colors.primary.toColor(),
        unfocusedBorderColor = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.2f),

        focusedLabelColor = AppTheme.colors.primary.toColor(),
        unfocusedLabelColor = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.6f),

        cursorColor = AppTheme.colors.primary.toColor(),
        focusedTextColor = AppTheme.colors.textPrimary.toColor(),
        unfocusedTextColor = AppTheme.colors.textPrimary.toColor(),

        // Error states
        errorBorderColor = AppTheme.colors.error.toColor(),
        errorLabelColor = AppTheme.colors.error.toColor(),
        errorCursorColor = AppTheme.colors.error.toColor()
    )

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, style = AppTheme.typo.caption) },
        placeholder = { Text(text = placeholder, style = AppTheme.typo.body) },
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