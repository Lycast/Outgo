package fr.abknative.outgo.android.components.month

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.designsystem.components.feedback.InfoTooltip
import fr.abknative.outgo.android.designsystem.components.layout.CardSplitSkeleton
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.ListLabels
import fr.abknative.outgo.android.ui.extensions.uiAmount

@Composable
fun StatsCardWallet(
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
        modifier = modifier.padding(horizontal = AppTheme.dimens.large, vertical = AppTheme.dimens.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.medium)
    ) {

        Text(
            text = activeWalletName,
            style = AppTheme.typo.subtitle,
            fontWeight = FontWeight.Medium,
            color = AppTheme.colors.textPrimary.toColor(),
        )

        CardSplitSkeleton(
            dataContent = {
                InfoTooltip( // todo le tooltip ne fonctione pas
                    title = ListLabels.TOOLTIP_TOTAL_INCOME_TITLE,
                    description = ListLabels.TOOLTIP_TOTAL_INCOME_DESC,
                ) {
                    HeroStatItem(
                        iconRes = R.drawable.bank_duotone,
                        amount = monthlyIncomeInCents.uiAmount,
                        liveColor = AppTheme.colors.secondary.toColor()
                    )
                }
            },
            visualContent = {
                InfoTooltip(
                    title = ListLabels.TOOLTIP_BALANCE_TITLE,
                    description = ListLabels.TOOLTIP_BALANCE_DESC,
                ) {
                    HeroStatItem(
                        iconRes = R.drawable.piggy_bank_duotone,
                        amount = disposableIncomeInCents.uiAmount,
                        liveColor = liveColor,
                        fontWeight = if (isNegativeLive) FontWeight.SemiBold else FontWeight.Bold,
                    )
                }
            }
        )
    }
}