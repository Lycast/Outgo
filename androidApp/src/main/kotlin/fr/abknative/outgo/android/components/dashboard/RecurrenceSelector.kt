package fr.abknative.outgo.android.components.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.abknative.outgo.android.components.common.SegmentedControl
import fr.abknative.outgo.android.ui.FormLabels
import fr.abknative.outgo.wallet.api.model.operation.Recurrence

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

    SegmentedControl(
        modifier = modifier,
        items = labels,
        selectedIndex = selectedIndex,
        onItemSelected = { index ->
            onRecurrenceChanged(recurrences[index])
        }
    )
}