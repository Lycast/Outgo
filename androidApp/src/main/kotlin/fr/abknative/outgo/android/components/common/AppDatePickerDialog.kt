package fr.abknative.outgo.android.components.common

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor
import fr.abknative.outgo.core.api.EpochMillis

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDatePickerDialog(
    datePickerState: DatePickerState,
    onDateSelected: (EpochMillis) -> Unit,
    onDismissRequest: () -> Unit
) {
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let { millis ->
                    onDateSelected(millis)
                }
                onDismissRequest()
            }) {
                Text("OK", color = AppTheme.colors.primary.toColor())
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Annuler", color = AppTheme.colors.textSecondary.toColor())
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = AppTheme.colors.surface200.toColor()
        )
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                titleContentColor = AppTheme.colors.textPrimary.toColor(),
                headlineContentColor = AppTheme.colors.primary.toColor(),
                weekdayContentColor = AppTheme.colors.textSecondary.toColor(),
                dayContentColor = AppTheme.colors.textPrimary.toColor(),
                selectedDayContainerColor = AppTheme.colors.primary.toColor(),
                selectedDayContentColor = AppTheme.colors.textOnBrand.toColor(),
                todayContentColor = AppTheme.colors.primary.toColor(),
                todayDateBorderColor = AppTheme.colors.primary.toColor()
            )
        )
    }
}