package fr.abknative.outgo.android.ui.month.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.core.components.cards.GlassCard
import fr.abknative.outgo.android.core.components.charts.AppDonutChart
import fr.abknative.outgo.android.core.components.layout.CardSplitSkeleton
import fr.abknative.outgo.android.core.designsystem.AppText
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor
import fr.abknative.outgo.android.core.extensions.uiAmount
import fr.abknative.outgo.android.ui.month.MonthLabels

@Composable
fun StatsCardExpense(
    modifier: Modifier = Modifier,
    totalOutgoingsInCents: Long,
    remainingToPayInCents: Long,
    progress: Float
) {

    Column(modifier = modifier) {
        AppText(
            text = MonthLabels.SECTION_EXPENSES.uppercase(),
            style = AppTheme.typo.label.copy(fontWeight = FontWeight.Bold),
            color = AppTheme.colors.primary.toColor(),
            modifier = Modifier.padding(
                bottom = AppTheme.dimens.small,
                start = AppTheme.dimens.medium
            )
        )

        GlassCard {
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
                                text = MonthLabels.HERO_TOTAL_CHARGES_LABEL,
                                style = AppTheme.typo.label
                            )

                            AppText(
                                text = totalOutgoingsInCents.uiAmount,
                                style = AppTheme.typo.subtitle,
                                color = AppTheme.colors.primary.toColor().copy(alpha = 0.8f)
                            )

                            Spacer(modifier = Modifier.height(AppTheme.dimens.extraSmall))

                            AppText(
                                text = MonthLabels.HERO_REMAINING_TO_PAY_LABEL,
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
    }
}