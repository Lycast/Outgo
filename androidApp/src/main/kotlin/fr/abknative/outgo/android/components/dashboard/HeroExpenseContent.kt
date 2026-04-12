package fr.abknative.outgo.android.components.dashboard

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.components.charts.AppDonutChart
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.DashboardLabels
import fr.abknative.outgo.android.ui.extensions.uiAmount

/**
 * A carousel page focused on expenses visualization using a donut chart.
 * Shows the ratio between total outgoings and what's left to pay.
 */
@SuppressLint("SuspiciousIndentation")
@Composable
fun HeroExpenseContent(
    totalOutgoingsInCents: Long,
    remainingToPayInCents: Long,
) {
    val paidAmountInCents = totalOutgoingsInCents - remainingToPayInCents
    val progress = if (totalOutgoingsInCents > 0) {
        paidAmountInCents.toFloat() / totalOutgoingsInCents
    } else 0f

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.dimens.extraLarge)
            .padding(vertical = AppTheme.dimens.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        // Visual Focus: The Donut
        AppDonutChart(
            progress = progress,
            activeColor = AppTheme.colors.primary.toColor(),
            trackColor = AppTheme.colors.tertiary.toColor(),
            strokeWidth = AppTheme.dimens.big,
            modifier = Modifier
                .size(100.dp)
                .alpha(0.2f)
        )

        // Data Details
        Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.extraSmall)) {

            Text(
                text = DashboardLabels.HERO_TOTAL_CHARGES_LABEL,
                style = AppTheme.typo.caption,
                color = AppTheme.colors.primary.toColor()
            )

            Text(
                text = totalOutgoingsInCents.uiAmount,
                style = AppTheme.typo.body,
                color = AppTheme.colors.textPrimary.toColor()
            )

            Spacer(modifier = Modifier.height(AppTheme.dimens.extraSmall))

            Text(
                text = DashboardLabels.HERO_REMAINING_TO_PAY_LABEL,
                style = AppTheme.typo.caption,
                color = AppTheme.colors.tertiary.toColor()
            )

            Text(
                text = remainingToPayInCents.uiAmount,
                style = AppTheme.typo.body,
                fontWeight = FontWeight.Medium,
                color = AppTheme.colors.textPrimary.toColor()
            )
        }
    }
}