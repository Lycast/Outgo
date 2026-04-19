package fr.abknative.outgo.android.designsystem.components.feedback

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import fr.abknative.outgo.android.designsystem.components.inputs.AppDatePickerDialog
import fr.abknative.outgo.android.designsystem.components.inputs.AppTextField
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.AccessibilityLabels
import fr.abknative.outgo.android.ui.OperationLabels
import fr.abknative.outgo.android.ui.extensions.DateTransformation
import fr.abknative.outgo.core.api.time.EpochMillis

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormattedDateInput(
    value: String,
    onValueChange: (String) -> Unit,
    onDateSelected: (EpochMillis) -> Unit,
    initialDateMillis: EpochMillis?,
    label: String,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.NumberPassword,
        imeAction = ImeAction.Done
    )
) {
    var showDatePicker by remember { mutableStateOf(false) }

    AppTextField(
        value = value,
        onValueChange = { input ->
            val digitsOnly = input.filter { it.isDigit() }.take(8)
            onValueChange(digitsOnly)
        },
        label = label,
        placeholder = OperationLabels.PLACEHOLDER_DATE_FORMAT,
        isError = isError,
        keyboardOptions = keyboardOptions,
        visualTransformation = DateTransformation(),
        trailingIcon = {
            IconButton(
                onClick = { showDatePicker = true },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f),
                )
            ) {
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