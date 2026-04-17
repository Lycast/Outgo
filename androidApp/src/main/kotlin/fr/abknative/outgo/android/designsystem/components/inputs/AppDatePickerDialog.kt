package fr.abknative.outgo.android.designsystem.components.inputs

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import fr.abknative.outgo.android.designsystem.components.buttons.AppTextButton
import fr.abknative.outgo.android.designsystem.foundation.AppText
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.CommonLabels
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
        shape = AppTheme.shapes.extraLarge,
        confirmButton = {
            AppTextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        onDateSelected(millis)
                    }
                    onDismissRequest()
                }
            ) {
                AppText(CommonLabels.ACTION_OK)
            }
        },
        dismissButton = {
            AppTextButton(
                onClick = onDismissRequest
            ) {
                AppText(CommonLabels.ACTION_CANCEL)
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = AppTheme.colors.surface200.toColor()
        )
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                containerColor = AppTheme.colors.surface200.toColor(),
                titleContentColor = AppTheme.colors.textPrimary.toColor(),
                headlineContentColor = AppTheme.colors.primary.toColor(),
                weekdayContentColor = AppTheme.colors.textSecondary.toColor(),
                dayContentColor = AppTheme.colors.textPrimary.toColor(),
                selectedDayContainerColor = AppTheme.colors.primary.toColor(),
                selectedDayContentColor = AppTheme.colors.textOnBrand.toColor(),
                todayContentColor = AppTheme.colors.primary.toColor(),
                todayDateBorderColor = AppTheme.colors.primary.toColor(),
                selectedYearContainerColor = AppTheme.colors.primary.toColor(),
                selectedYearContentColor = AppTheme.colors.textOnBrand.toColor()
            )
        )
    }
}