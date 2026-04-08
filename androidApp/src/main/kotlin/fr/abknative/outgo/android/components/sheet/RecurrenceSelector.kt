package fr.abknative.outgo.android.components.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import fr.abknative.outgo.android.components.common.SegmentedControl
import fr.abknative.outgo.android.ui.FormLabels
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor
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

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Sélectionnez la récurrence", // TODO: Extract to FormLabels
            style = AppTheme.typo.caption,
            color = AppTheme.colors.textSecondary.toColor(),
            modifier = Modifier.padding(horizontal = AppTheme.spacing.small)
        )

        Spacer(modifier = Modifier.height(AppTheme.spacing.small))

        SegmentedControl(
            modifier = modifier,
            items = labels,
            selectedIndex = selectedIndex,
            onItemSelected = { index ->
                onRecurrenceChanged(recurrences[index])
            }
        )
    }
}