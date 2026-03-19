package fr.abknative.outgo.android.components.dashboard

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.components.common.CircleIcon
import fr.abknative.outgo.android.components.common.InfoTooltip
import fr.abknative.outgo.android.ui.DashboardLabels
import fr.abknative.outgo.android.ui.extensions.uiAmount
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.OutgoTheme
import fr.abknative.outgo.android.ui.theme.toColor


@Composable
fun HeroSection(
    isExpanded: Boolean,
    formattedMonthDate: String,
    monthlyIncomeInCents: Long,
    totalOutgoingsInCents: Long,
    disposableIncomeInCents: Long,
    remainingToPayInCents: Long,
    onToggleExpand: () -> Unit,
    onEditBudgetClick: () -> Unit,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit
) {
    val maxValue = maxOf(monthlyIncomeInCents, totalOutgoingsInCents).coerceAtLeast(1L).toFloat()
    val isNegativeLive = disposableIncomeInCents < 0
    val liveColor = if (isNegativeLive) AppTheme.colors.error.toColor() else AppTheme.colors.tertiary.toColor()

    val totalLength = monthlyIncomeInCents.uiAmount.length + disposableIncomeInCents.uiAmount.length
    val isTooLong = totalLength > 18

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.spacing.medium),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface100.toColor()),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MonthBudgetSelector(
                formattedMonthDate = formattedMonthDate,
                onPreviousMonthClick = onPreviousMonthClick,
                onNextMonthClick = onNextMonthClick
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = AppTheme.spacing.large),
                color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f)
            )

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.spacing.large),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.extraLarge),
                ) {
                    Spacer(modifier = Modifier.height(AppTheme.spacing.small))

                    val content = @Composable {
                        BudgetItem(
                            amount = monthlyIncomeInCents.uiAmount,
                            onClick = onEditBudgetClick
                        )
                        Spacer(modifier = Modifier.width(AppTheme.spacing.large))
                        LiveItem(
                            isNegativeLive = isNegativeLive,
                            amount = disposableIncomeInCents.uiAmount,
                            color = liveColor,
                            fontWeight = if (isNegativeLive) FontWeight.Medium else FontWeight.Bold
                        )
                    }

                    if (isTooLong) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.medium)
                        ) {
                            content()
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            content()
                        }
                    }

                    HorizontalDivider(color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f))

                    PairedBudgetBar(
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
            }

            // Bouton de déploiement
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpand() }
                    .padding(vertical = AppTheme.spacing.small),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = if (isExpanded) R.drawable.caret_up else R.drawable.caret_down),
                    contentDescription = null,
                    tint = AppTheme.colors.textSecondary.toColor(),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}


@Composable
private fun BudgetItem(
    amount: String,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleIcon(
            icon = R.drawable.bank_duotone,
            tintColor = AppTheme.colors.textOnBrand.toColor(),
            containerColor = AppTheme.colors.primary.toColor()
        )
        Column(
            modifier = Modifier.clickable(onClick = onClick),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = DashboardLabels.HERO_TOTAL_INCOME_LABEL,
                style = AppTheme.typo.caption,
                color = AppTheme.colors.textSecondary.toColor(),
                fontWeight = FontWeight.Medium
            )

            Text(
                text = amount,
                style = AppTheme.typo.title,
                color = AppTheme.colors.textPrimary.toColor()
            )
        }
    }
}

@Composable
private fun LiveItem(
    isNegativeLive: Boolean,
    amount: String,
    color: Color,
    fontWeight: FontWeight
) {
    InfoTooltip(
        title = DashboardLabels.TOOLTIP_BALANCE_TITLE,
        description = DashboardLabels.TOOLTIP_BALANCE_DESC,
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleIcon(
                icon = R.drawable.piggy_bank_duotone,
                tintColor = AppTheme.colors.textOnBrand.toColor(),
                containerColor = color
            )
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = if (isNegativeLive) DashboardLabels.HERO_MISSING_INCOME_LABEL else DashboardLabels.HERO_DISPOSABLE_INCOME_LABEL,
                    style = AppTheme.typo.caption,
                    color = AppTheme.colors.textSecondary.toColor(),
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = amount,
                    style = AppTheme.typo.title,
                    color = color,
                    fontWeight = fontWeight
                )
            }
        }
    }
}


@Preview(showBackground = true, name = "Hero Section - Cas Nominal")
@Composable
fun PreviewHeroSectionNominal() {
    OutgoTheme {
        HeroSection(
            formattedMonthDate = "Mars",
            monthlyIncomeInCents = 65000L, // 650.00€
            totalOutgoingsInCents = 12000L, // 120.00€
            disposableIncomeInCents = 30000L, // 300.00€
            remainingToPayInCents = 45000L,   // 450.00€
            onToggleExpand = { /* Action de test */ },
            onPreviousMonthClick = { /* Action de test */ },
            onNextMonthClick = { /* Action de test */ },
            onEditBudgetClick = { /* Action de test */ },
            isExpanded = true
        )
    }
}

@Preview(
    showBackground = true,
    name = "Hero Section - Budget Négatif",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PreviewHeroSectionNegative() {
    OutgoTheme {
        HeroSection(
            formattedMonthDate = "Mars",
            monthlyIncomeInCents = 15005490000L,  // 150 054 900.00€
            totalOutgoingsInCents = 18059000000L,  // 180 590 000.00€
            disposableIncomeInCents = -390000000L, // -3 900 000.00€ (Alerte rouge)
            remainingToPayInCents = 2000000000L,    // 2000000.00€
            onToggleExpand = { /* Action de test */ },
            onPreviousMonthClick = { /* Action de test */ },
            onNextMonthClick = { /* Action de test */ },
            onEditBudgetClick = { /* Action de test */ },
            isExpanded = true
        )
    }
}