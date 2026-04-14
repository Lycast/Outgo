package fr.abknative.outgo.android.components.month

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.components.charts.AppDonutChart
import fr.abknative.outgo.android.designsystem.components.layout.CardSplitSkeleton
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.ListLabels
import fr.abknative.outgo.android.ui.extensions.uiAmount

/**
 * A carousel page focused on expenses visualization using a donut chart.
 * Shows the ratio between total outgoings and what's left to pay.
 *
 * @param totalOutgoingsInCents The total expenses planned for the month.
 * @param remainingToPayInCents The amount left to pay.
 */
@Composable
fun StatsCardExpense(
    totalOutgoingsInCents: Long,
    remainingToPayInCents: Long,
) {
    val progress = if (totalOutgoingsInCents > 0) {
        remainingToPayInCents.toFloat() / totalOutgoingsInCents.toFloat()
    } else 0f

    Column(
        modifier = Modifier.padding(AppTheme.dimens.large),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CardSplitSkeleton(
            dataContent = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.extraSmall),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = ListLabels.HERO_TOTAL_CHARGES_LABEL,
                        style = AppTheme.typo.label,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.colors.textPrimary.toColor()
                    )

                    Text(
                        text = totalOutgoingsInCents.uiAmount,
                        style = AppTheme.typo.title.copy(
                            fontSize = AppTheme.typo.title.fontSize * 0.8
                        ),
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.colors.primary.toColor()
                    )

                    Spacer(modifier = Modifier.height(AppTheme.dimens.extraSmall))

                    Text(
                        text = ListLabels.HERO_REMAINING_TO_PAY_LABEL,
                        style = AppTheme.typo.label,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.colors.textPrimary.toColor()
                    )

                    Text(
                        text = remainingToPayInCents.uiAmount,
                        style = AppTheme.typo.title.copy(
                            fontSize = AppTheme.typo.title.fontSize * 0.8
                        ),
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.colors.tertiary.toColor()
                    )
                }
            },
            visualContent = {
                AppDonutChart(
                    progress = progress,
                    isMirrored = true,
                    activeColor = AppTheme.colors.tertiary.toColor(),
                    trackColor = AppTheme.colors.primary.toColor().copy(alpha = 0.5f),
                    strokeWidth = AppTheme.dimens.large,
                    modifier = Modifier
                        .size(90.dp)
                        .alpha(0.4f)
                )
            }
        )
    }
}