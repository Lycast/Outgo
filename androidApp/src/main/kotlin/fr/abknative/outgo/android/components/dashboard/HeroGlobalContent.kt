package fr.abknative.outgo.android.components.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.designsystem.components.charts.AppDonutChart
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
    onEditBudgetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isNegativeLive = disposableIncomeInCents < 0
    val liveColor = if (isNegativeLive) AppTheme.colors.error.toColor() else AppTheme.colors.tertiary.toColor()

    val donutProgress = if (monthlyIncomeInCents > 0) {
        if (isNegativeLive) {
            ((totalOutgoingsInCents - monthlyIncomeInCents).toFloat() / monthlyIncomeInCents.toFloat())
                .coerceAtMost(1f)
        } else {
            (disposableIncomeInCents.toFloat() / monthlyIncomeInCents.toFloat())
                .coerceAtMost(1f)
        }
    } else 0f

    Column(
        modifier = Modifier
            .height(160.dp)
            .padding(vertical = AppTheme.dimens.small),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Vue sur le budget",
            style = AppTheme.typo.caption,
            fontWeight = FontWeight.Medium,
            color = AppTheme.colors.textPrimary.toColor()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimens.extraLarge)
                .padding(top = AppTheme.dimens.large),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {

            Column(
                modifier = Modifier.weight(1f, fill = false),
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.small)
            ) {
                HeroStatItem(
                    iconRes = R.drawable.bank_duotone,
                    label = activeWalletName,
                    amount = monthlyIncomeInCents.uiAmount,
                    liveColor = AppTheme.colors.secondary.toColor(),
                    onClick = onEditBudgetClick,
                )

                InfoTooltip(
                    title = DashboardLabels.TOOLTIP_BALANCE_TITLE,
                    description = DashboardLabels.TOOLTIP_BALANCE_DESC,
                ) {
                    HeroStatItem(
                        iconRes = R.drawable.piggy_bank_duotone,
                        label = if (isNegativeLive) DashboardLabels.HERO_MISSING_INCOME_LABEL else DashboardLabels.HERO_DISPOSABLE_INCOME_LABEL,
                        amount = disposableIncomeInCents.uiAmount,
                        liveColor = liveColor,
                        fontWeight = if (isNegativeLive) FontWeight.SemiBold else FontWeight.Bold,
                    )
                }
            }

            Spacer(modifier = Modifier.width(AppTheme.dimens.extraLarge))

            Box(
                modifier = Modifier.weight(0.7f, fill = false),
            ) {
                AppDonutChart(
                    progress = donutProgress,
                    activeColor = liveColor,
                    trackColor = AppTheme.colors.secondary.toColor().copy(alpha = 0.5f),
                    strokeWidth = AppTheme.dimens.large,
                    modifier = Modifier
                        .size(90.dp)
                        .alpha(0.8f)
                )
            }
        }
    }
}