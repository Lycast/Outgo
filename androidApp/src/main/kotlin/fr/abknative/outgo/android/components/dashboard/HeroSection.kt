package fr.abknative.outgo.android.components.dashboard

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.components.common.InfoTooltip
import fr.abknative.outgo.android.ui.AccessibilityLabels
import fr.abknative.outgo.android.ui.DashboardLabels
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.OutgoTheme
import fr.abknative.outgo.android.ui.uiAmount


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
    val liveColor = if (isNegativeLive) MaterialTheme.colorScheme.error else AppTheme.dashboardColors.remainingLive

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.spacing.medium),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)),
        shape = MaterialTheme.shapes.medium,
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- EN-TETE : Mois + Sélecteur ---
            MonthBudgetSelector(
                formattedMonthDate = formattedMonthDate,
                onPreviousMonthClick = onPreviousMonthClick,
                onNextMonthClick = onNextMonthClick
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = AppTheme.spacing.large),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
            )

            // 2. Animation de visibilité pour les Blocs 1 et 2
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

                    // --- BLOC 1 : Budget vs Reste à vivre ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        // --- budget ---
                        Row(modifier = Modifier.clickable(onClick = onEditBudgetClick)) {
                            Icon(
                                imageVector = Icons.Default.AccountBalance,
                                contentDescription = AccessibilityLabels.EDIT_BUDGET,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.width(AppTheme.spacing.medium))
                            Text(
                                text = monthlyIncomeInCents.uiAmount,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // --- Reste à vivre ---
                        InfoTooltip(
                            title = DashboardLabels.TOOLTIP_BALANCE_TITLE,
                            description = DashboardLabels.TOOLTIP_BALANCE_DESC,
                        ) {
                            Row {
                                Icon(
                                    imageVector = Icons.Default.Balance,
                                    contentDescription = AccessibilityLabels.ICON_BALANCE,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(Modifier.width(AppTheme.spacing.medium))
                                Text(
                                    text = disposableIncomeInCents.uiAmount,
                                    fontWeight = if (isNegativeLive) FontWeight.Medium else FontWeight.Bold,
                                    color = liveColor
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f))

                    // --- BLOC 2 : Charges Totales vs Reste à payer ---
                    PairedBudgetBar(
                        topLabel = DashboardLabels.HERO_TOTAL_CHARGES_LABEL,
                        topAmount = totalOutgoingsInCents.uiAmount,
                        topProgress = totalOutgoingsInCents / maxValue,
                        topBarColor = AppTheme.dashboardColors.charges,

                        bottomLabel = DashboardLabels.HERO_REMAINING_TO_PAY_LABEL,
                        bottomAmount = remainingToPayInCents.uiAmount,
                        bottomProgress = remainingToPayInCents / maxValue,
                        bottomBarColor = AppTheme.dashboardColors.remainingPay
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpand() }
                    .padding(vertical = AppTheme.spacing.small),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) AccessibilityLabels.COLLAPSE_HERO else AccessibilityLabels.EXPAND_HERO,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.size(24.dp)
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
            monthlyIncomeInCents = 250000L, // 2500.00€
            totalOutgoingsInCents = 120000L, // 1200.00€
            disposableIncomeInCents = 130000L, // 1300.00€
            remainingToPayInCents = 45000L,   // 450.00€
            onToggleExpand = { /* Action de test */ },
            onPreviousMonthClick = { /* Action de test */ },
            onNextMonthClick = { /* Action de test */ },
            onEditBudgetClick = { /* Action de test */  },
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
            monthlyIncomeInCents = 150000L,  // 1500.00€
            totalOutgoingsInCents = 180000L,  // 1800.00€
            disposableIncomeInCents = -30000L, // -300.00€ (Alerte rouge)
            remainingToPayInCents = 20000L,    // 200.00€
            onToggleExpand = { /* Action de test */ },
            onPreviousMonthClick = { /* Action de test */ },
            onNextMonthClick = { /* Action de test */ },
            onEditBudgetClick = { /* Action de test */  },
            isExpanded = true
        )
    }
}