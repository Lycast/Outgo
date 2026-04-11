package fr.abknative.outgo.android.designsystem.components.feedback

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import fr.abknative.outgo.android.designsystem.components.inputs.AppDatePickerDialog
import fr.abknative.outgo.android.designsystem.components.inputs.AppTextField
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.AccessibilityLabels
import fr.abknative.outgo.android.ui.extensions.DateTransformation
import fr.abknative.outgo.core.api.EpochMillis

/**
 * A specialized input field for dates that uses [AppTextField] for visual consistency.
 * It features a numeric mask (dd/mm/yyyy) and a trailing icon to trigger an [AppDatePickerDialog].
 *
 * @param value The raw numeric string buffer (e.g., "08042026").
 * @param onValueChange Callback invoked when the user types.
 * @param onDateSelected Callback invoked when a date is picked from the calendar.
 * @param initialDateMillis The optional timestamp to pre-select in the calendar.
 * @param label The text displayed on the input field.
 * @param modifier The layout modifier.
 * @param isError True if the current buffer is invalid.
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

    // Using our design system's AppTextField
    AppTextField(
        value = value,
        onValueChange = { input ->
            val digitsOnly = input.filter { it.isDigit() }.take(8)
            onValueChange(digitsOnly)
        },
        label = label,
        placeholder = "JJ/MM/AAAA",
        isError = isError,
        keyboardOptions = keyboardOptions,
        visualTransformation = DateTransformation(), // Our custom mask
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