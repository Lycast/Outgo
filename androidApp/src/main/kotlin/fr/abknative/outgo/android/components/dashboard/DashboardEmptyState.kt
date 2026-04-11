package fr.abknative.outgo.android.components.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.abknative.outgo.android.designsystem.components.feedback.AppEmptyState
import fr.abknative.outgo.android.ui.DashboardLabels
import fr.abknative.outgo.dashboard.api.OperationFilter

/**
 * Specialized empty state for the dashboard, mapping filters to specific messages.
 */
@Composable
fun DashboardEmptyState(
    filter: OperationFilter,
    modifier: Modifier = Modifier
) {
    val title = when (filter) {
        OperationFilter.ALL -> DashboardLabels.EMPTY_ALL
        OperationFilter.PAST -> DashboardLabels.EMPTY_PAID
        OperationFilter.REMAINING -> DashboardLabels.EMPTY_REMAINING
    }

    val description = if (filter == OperationFilter.ALL) {
        DashboardLabels.EMPTY_STATE_DESC
    } else null

    AppEmptyState(
        icon = Icons.Rounded.Info,
        title = title,
        description = description,
        modifier = modifier
    )
}