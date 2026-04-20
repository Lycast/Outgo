package fr.abknative.outgo.android.ui.month.month

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.core.CommonLabels
import fr.abknative.outgo.android.core.R
import fr.abknative.outgo.android.core.components.layout.CardSplitSkeleton
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor
import fr.abknative.outgo.android.core.extensions.uiAmount

@Composable
fun StatsCardWallet(
    activeWalletName: String,
    monthlyIncomeInCents: Long,
    disposableIncomeInCents: Long,
    onEditBudgetClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val isNegativeLive = disposableIncomeInCents < 0
    val liveColor = if (isNegativeLive) AppTheme.colors.error.toColor() else AppTheme.colors.tertiary.toColor()

    Column(
        modifier = modifier.padding(horizontal = AppTheme.dimens.large, vertical = AppTheme.dimens.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.medium)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = activeWalletName,
                style = AppTheme.typo.subtitle,
                fontWeight = FontWeight.Medium,
                color = AppTheme.colors.textPrimary.toColor(),
            )

            Icon(
                painter = painterResource(R.drawable.pencil_simple),
                contentDescription = CommonLabels.ACTION_EDIT,
                modifier = Modifier
                    .padding(horizontal = AppTheme.dimens.small)
                    .clip(CircleShape)
                    .size(16.dp)
                    .align(Alignment.CenterEnd)
                    .clickable { onEditBudgetClick() },
            )
        }

        CardSplitSkeleton(
            leftContent = {
                HeroStatItem(
                    iconRes = R.drawable.bank_duotone,
                    amount = monthlyIncomeInCents.uiAmount,
                    liveColor = AppTheme.colors.secondary.toColor()
                )
            },
            rightContent = {
                HeroStatItem(
                    iconRes = R.drawable.piggy_bank_duotone,
                    amount = disposableIncomeInCents.uiAmount,
                    liveColor = liveColor,
                    fontWeight = if (isNegativeLive) FontWeight.Medium else FontWeight.Bold,
                )
            }
        )
    }
}