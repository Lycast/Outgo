package fr.abknative.outgo.android.components.month

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.components.charts.AppDonutChart
import fr.abknative.outgo.android.designsystem.components.layout.CardSplitSkeleton
import fr.abknative.outgo.android.designsystem.foundation.AppText
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.ListLabels
import fr.abknative.outgo.android.ui.extensions.uiAmount

@Composable
fun StatsCardExpense(
    totalOutgoingsInCents: Long,
    remainingToPayInCents: Long,
    progress: Float
) {

    Column(
        modifier = Modifier.padding(AppTheme.dimens.large),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CardSplitSkeleton(
            leftContent = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.extraSmall),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    AppText(
                        text = ListLabels.HERO_TOTAL_CHARGES_LABEL,
                        style = AppTheme.typo.label
                    )

                    AppText(
                        text = totalOutgoingsInCents.uiAmount,
                        style = AppTheme.typo.subtitle,
                        color = AppTheme.colors.primary.toColor().copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(AppTheme.dimens.extraSmall))

                    AppText(
                        text = ListLabels.HERO_REMAINING_TO_PAY_LABEL,
                        style = AppTheme.typo.label,
                    )

                    AppText(
                        text = remainingToPayInCents.uiAmount,
                        style = AppTheme.typo.subtitle,
                        color = AppTheme.colors.tertiary.toColor().copy(alpha = 0.8f)
                    )
                }
            },
            rightContent = {
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