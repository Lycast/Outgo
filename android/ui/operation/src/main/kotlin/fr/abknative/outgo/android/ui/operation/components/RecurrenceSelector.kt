package fr.abknative.outgo.android.ui.operation.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import fr.abknative.outgo.android.core.FormLabels
import fr.abknative.outgo.android.core.components.selection.AppSegmentedControl
import fr.abknative.outgo.android.core.designsystem.AppText
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor
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

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppText(
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