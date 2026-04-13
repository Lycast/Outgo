package fr.abknative.outgo.android.components.list

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.abknative.outgo.android.designsystem.components.feedback.AppEmptyState
import fr.abknative.outgo.android.ui.ListLabels
import fr.abknative.outgo.list.api.OperationFilter

/**
 * Specialized empty state for the dashboard, mapping filters to specific messages.
 */
@Composable
fun ListEmptyState(
    filter: OperationFilter,
    modifier: Modifier = Modifier
) {
    val title = when (filter) {
        OperationFilter.ALL -> ListLabels.EMPTY_ALL
        OperationFilter.PAST -> ListLabels.EMPTY_PAID
        OperationFilter.REMAINING -> ListLabels.EMPTY_REMAINING
    }

    val description = if (filter == OperationFilter.ALL) {
        ListLabels.EMPTY_STATE_DESC
    } else null

    AppEmptyState(
        icon = Icons.Rounded.Info,
        title = title,
        description = description,
        modifier = modifier
    )
}