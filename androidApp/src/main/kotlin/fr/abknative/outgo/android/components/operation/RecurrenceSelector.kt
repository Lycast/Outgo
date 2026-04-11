package fr.abknative.outgo.android.components.operation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import fr.abknative.outgo.android.designsystem.components.selection.AppSegmentedControl
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.FormLabels
import fr.abknative.outgo.wallet.api.model.operation.Recurrence

/**
 * A specialized selector for [Recurrence] cycles.
 * Maps the business logic enum to localized labels and displays them
 * using the design system's [AppSegmentedControl].
 *
 * @param selectedRecurrence The currently active [Recurrence] state.
 * @param onRecurrenceChanged Callback invoked when a new cycle is selected.
 * @param modifier The modifier to be applied to the container.
 */
@Composable
fun RecurrenceSelector(
    selectedRecurrence: Recurrence,
    onRecurrenceChanged: (Recurrence) -> Unit,
    modifier: Modifier = Modifier
) {

    val recurrences = listOf(
        Recurrence.UNIQUE,
        Recurrence.WEEKLY,
        Recurrence.MONTHLY,
        Recurrence.YEARLY
    )

    val labels = listOf(
        FormLabels.CYCLE_UNIQUE,
        FormLabels.CYCLE_WEEKLY,
        FormLabels.CYCLE_MONTHLY,
        FormLabels.CYCLE_YEARLY
    )


    val selectedIndex = recurrences.indexOf(selectedRecurrence).coerceAtLeast(0)

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = FormLabels.FIELD_RECURRENCE_DESC,
            style = AppTheme.typo.caption,
            color = AppTheme.colors.textSecondary.toColor(),
            modifier = Modifier.padding(horizontal = AppTheme.dimens.small)
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.small))

        AppSegmentedControl(
            items = labels,
            selectedIndex = selectedIndex,
            onItemSelected = { index ->
                onRecurrenceChanged(recurrences[index])
            }
        )
    }
}