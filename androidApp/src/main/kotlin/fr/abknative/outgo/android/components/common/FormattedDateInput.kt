package fr.abknative.outgo.android.components.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import fr.abknative.outgo.android.ui.AccessibilityLabels
import fr.abknative.outgo.android.ui.extensions.DateTransformation
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor
import fr.abknative.outgo.core.api.EpochMillis

/**
 * A formatted date input field that enforces a numeric "ddMMyyyy" buffer state
 * while displaying a visual "dd/MM/yyyy" mask to the user.
 *
 * It strictly delegates all time-related formatting and logic back to the caller
 * to respect the clean architecture boundaries.
 *
 * @param value The raw numeric string buffer (e.g., "08042026").
 * @param onValueChange Callback invoked when the user types in the text field.
 * @param onDateSelected Callback invoked when the user picks a date from the native calendar.
 * @param initialDateMillis The optional timestamp to pre-select in the calendar dialog.
 * @param label The text displayed on the input field.
 * @param modifier The layout modifier.
 * @param isError True if the current buffer does not represent a valid date.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormattedDateInput(
    value: String,
    onValueChange: (String) -> Unit,
    onDateSelected: (EpochMillis) -> Unit,
    initialDateMillis: EpochMillis?,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.NumberPassword,
        imeAction = ImeAction.Done
    )
) {
    var showDatePicker by remember { mutableStateOf(false) }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = AppTheme.colors.surface50.toColor().copy(alpha = 0.2f),
        focusedBorderColor = AppTheme.colors.primary.toColor(),
        unfocusedBorderColor = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.2f),
        focusedLabelColor = AppTheme.colors.primary.toColor(),
        unfocusedLabelColor = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.6f),
        cursorColor = AppTheme.colors.primary.toColor(),
        focusedTextColor = AppTheme.colors.textPrimary.toColor(),
        unfocusedTextColor = AppTheme.colors.textPrimary.toColor(),
        errorBorderColor = AppTheme.colors.error.toColor(),
        errorLabelColor = AppTheme.colors.error.toColor(),
        errorTrailingIconColor = AppTheme.colors.error.toColor()
    )

    OutlinedTextField(
        value = value,
        onValueChange = { input ->
            val digitsOnly = input.filter { it.isDigit() }.take(8)
            onValueChange(digitsOnly)
        },
        label = { Text(text = label, style = AppTheme.typo.caption) },
        modifier = modifier.fillMaxWidth(),
        isError = isError,
        textStyle = AppTheme.typo.body,
        colors = textFieldColors,
        keyboardOptions = keyboardOptions,
        visualTransformation = DateTransformation(),
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = { showDatePicker = true }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = AccessibilityLabels.DAY_SELECTOR,
                    tint = AppTheme.colors.primary.toColor()
                )
            }
        }
    )

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialDateMillis
        )

        AppDatePickerDialog(
            datePickerState = datePickerState,
            onDateSelected = onDateSelected,
            onDismissRequest = { showDatePicker = false }
        )
    }
}