package fr.abknative.outgo.android.components.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.ui.AccessibilityLabels
import fr.abknative.outgo.android.ui.DashboardLabels
import fr.abknative.outgo.android.ui.states.OutgoingFilter
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor

@Composable
fun EmptyStateView(
    filter: OutgoingFilter,
    modifier: Modifier = Modifier
) {

    val message = when (filter) {
        OutgoingFilter.ALL -> DashboardLabels.EMPTY_ALL
        OutgoingFilter.PAID -> DashboardLabels.EMPTY_PAID
        OutgoingFilter.REMAINING -> DashboardLabels.EMPTY_REMAINING
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(AppTheme.spacing.extraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(AppTheme.spacing.large))

        Icon(
            imageVector = Icons.Rounded.Info,
            contentDescription = AccessibilityLabels.INFO_EMPTY_STATE,
            modifier = Modifier.size(32.dp),
            tint = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.4f)
        )

        Spacer(modifier = Modifier.height(AppTheme.spacing.extraLarge))

        Text(
            text = message,
            style = AppTheme.typo.subtitle,
            color = AppTheme.colors.textPrimary.toColor(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppTheme.spacing.medium))

        if (filter == OutgoingFilter.ALL) {
            Text(
                text = DashboardLabels.EMPTY_STATE_DESC,
                style = AppTheme.typo.body,
                color = AppTheme.colors.textSecondary.toColor(),
                textAlign = TextAlign.Center
            )
        }
    }
}