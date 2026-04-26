package fr.abknative.outgo.android.core.components.feedback

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import fr.abknative.outgo.android.core.AccessibilityLabels
import fr.abknative.outgo.android.core.CommonLabels
import fr.abknative.outgo.android.core.components.inputs.AppDatePickerDialog
import fr.abknative.outgo.android.core.components.inputs.AppTextField
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor
import fr.abknative.outgo.android.core.extensions.DateTransformation
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
        modifier = Modifier.padding(top = AppTheme.dimens.extraSmall),
        value = value,
        onValueChange = { input ->
            val digitsOnly = input.filter { it.isDigit() }.take(8)
            onValueChange(digitsOnly)
        },
        label = label,
        placeholder = CommonLabels.PLACEHOLDER_DATE_FORMAT,
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