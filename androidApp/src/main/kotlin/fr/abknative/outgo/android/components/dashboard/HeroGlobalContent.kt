package fr.abknative.outgo.android.components.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.designsystem.components.charts.AppDonutChart
import fr.abknative.outgo.android.designsystem.components.charts.AppPairedBar
import fr.abknative.outgo.android.designsystem.components.feedback.InfoTooltip
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.DashboardLabels
import fr.abknative.outgo.android.ui.extensions.uiAmount

@Composable
fun HeroGlobalContent(
    activeWalletName: String,
    monthlyIncomeInCents: Long,
    totalOutgoingsInCents: Long,
    disposableIncomeInCents: Long,
    remainingToPayInCents: Long,
    onEditBudgetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val maxValue = maxOf(monthlyIncomeInCents, totalOutgoingsInCents).coerceAtLeast(1L).toFloat()
    val isNegativeLive = disposableIncomeInCents < 0
    val liveColor = if (isNegativeLive) AppTheme.colors.error.toColor() else AppTheme.colors.tertiary.toColor()

    val donutProgress = if (monthlyIncomeInCents > 0) {
        totalOutgoingsInCents.toFloat() / monthlyIncomeInCents.toFloat()
    } else 0f

    Column(
        modifier = modifier.fillMaxWidth()
            .padding(horizontal = AppTheme.dimens.large)
    ) {

        Spacer(modifier = Modifier.height(AppTheme.dimens.small))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = AppTheme.dimens.large),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {

            Column(
                modifier = Modifier.weight(1f, fill = false),
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.small),
            ) {
                HeroStatItem(
                    iconRes = R.drawable.bank_duotone,
                    label = activeWalletName,
                    amount = monthlyIncomeInCents.uiAmount,
                    iconTint = AppTheme.colors.secondary.toColor(),
                    onClick = onEditBudgetClick,
                )

                HorizontalDivider(
                    modifier = Modifier.padding(end = AppTheme.dimens.large),
                    color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.05f)
                )

                InfoTooltip(
                    title = DashboardLabels.TOOLTIP_BALANCE_TITLE,
                    description = DashboardLabels.TOOLTIP_BALANCE_DESC,
                ) {
                    HeroStatItem(
                        iconRes = R.drawable.piggy_bank_duotone,
                        label = if (isNegativeLive) DashboardLabels.HERO_MISSING_INCOME_LABEL else DashboardLabels.HERO_DISPOSABLE_INCOME_LABEL,
                        amount = disposableIncomeInCents.uiAmount,
                        iconTint = liveColor,
                        amountColor = liveColor,
                        fontWeight = if (isNegativeLive) FontWeight.Medium else FontWeight.Bold,
                    )
                }
            }

            Box(
                modifier = Modifier.weight(1f, fill = false),
            ) {
                AppDonutChart(
                    progress = donutProgress,
                    activeColor = AppTheme.colors.secondary.toColor(),
                    trackColor = AppTheme.colors.tertiary.toColor(),
                    strokeWidth = AppTheme.dimens.extraLarge,
                    modifier = Modifier
                        .size(100.dp)
                        .alpha(0.3f)
                )
            }
        }

        HorizontalDivider(color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f))

        Box(
            modifier = Modifier.padding(vertical = AppTheme.dimens.medium)
        ) {
            AppPairedBar(
                topLabel = DashboardLabels.HERO_TOTAL_CHARGES_LABEL,
                topAmount = totalOutgoingsInCents.uiAmount,
                topProgress = totalOutgoingsInCents / maxValue,
                topBarColor = AppTheme.colors.primary.toColor(),

                bottomLabel = DashboardLabels.HERO_REMAINING_TO_PAY_LABEL,
                bottomAmount = remainingToPayInCents.uiAmount,
                bottomProgress = remainingToPayInCents / maxValue,
                bottomBarColor = AppTheme.colors.tertiary.toColor()
            )
        }
        Spacer(modifier = Modifier.height(AppTheme.dimens.small))
    }
}