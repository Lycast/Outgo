package fr.abknative.outgo.android.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.ui.DashboardLabels
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.OutgoTheme
import fr.abknative.outgo.android.ui.uiAmount
import kotlin.math.absoluteValue

@Composable
fun HeroSection(
    formattedTodayDate: String,
    monthlyIncomeInCents: Long,
    totalOutgoingsInCents: Long,
    disposableIncomeInCents: Long,
    remainingToPayInCents: Long,
    onEditIncomeClick: () -> Unit
) {

    val maxValue = maxOf(monthlyIncomeInCents, totalOutgoingsInCents).coerceAtLeast(1L).toFloat()
    val isNegativeLive = disposableIncomeInCents < 0
    val liveColor = if (isNegativeLive) MaterialTheme.colorScheme.error else AppTheme.dashboardColors.remainingLive
    val disposableRatio = (disposableIncomeInCents.absoluteValue / maxValue).coerceAtMost(1f)


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(AppTheme.spacing.medium),
    ) {

        Spacer(modifier = Modifier.height(AppTheme.spacing.large))

        Text(
            text = "${DashboardLabels.HERO_DATE_PREFIX} $formattedTodayDate".uppercase(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = AppTheme.spacing.small)
        )

        Spacer(modifier = Modifier.height(AppTheme.spacing.small))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
            shape = MaterialTheme.shapes.medium,
            onClick = onEditIncomeClick,

        ) {
            Column(
                modifier = Modifier
                    .padding(AppTheme.spacing.large)
                    .padding(bottom = AppTheme.spacing.extraLarge, top = AppTheme.spacing.medium),
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.extraLarge)
            ) {

                // --- Les 4 Barres de Progression ---
                // 1. BUDGET
                BudgetBarRow(
                    label = DashboardLabels.HERO_INCOME_LABEL,
                    amount = monthlyIncomeInCents.uiAmount,
                    progress = monthlyIncomeInCents / maxValue,
                    barColor = AppTheme.dashboardColors.budget
                )

                // 2. CHARGES TOTALES
                BudgetBarRow(
                    label = DashboardLabels.HERO_TOTAL_CHARGES_LABEL,
                    amount = totalOutgoingsInCents.uiAmount,
                    progress = totalOutgoingsInCents / maxValue,
                    barColor = AppTheme.dashboardColors.charges
                )

                // 3. RESTE À PAYER (Ce mois-ci)
                BudgetBarRow(
                    label = DashboardLabels.HERO_REMAINING_TO_PAY_LABEL,
                    amount = remainingToPayInCents.uiAmount,
                    progress = remainingToPayInCents / maxValue,
                    barColor = AppTheme.dashboardColors.remainingPay
                )

                // 4. RESTE À VIVRE (L'indicateur de santé)
                BudgetBarRow(
                    label = DashboardLabels.HERO_DISPOSABLE_INCOME_LABEL,
                    amount = disposableIncomeInCents.uiAmount,
                    progress = disposableRatio,
                    barColor = liveColor
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
            formattedTodayDate = "Mars",
            monthlyIncomeInCents = 250000L, // 2500.00€
            totalOutgoingsInCents = 120000L, // 1200.00€
            disposableIncomeInCents = 130000L, // 1300.00€
            remainingToPayInCents = 45000L,   // 450.00€
            onEditIncomeClick = { /* Action de test */ }
        )
    }
}

@Preview(
    showBackground = true,
    name = "Hero Section - Budget Négatif",
    //uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PreviewHeroSectionNegative() {
    OutgoTheme {
        HeroSection(
            formattedTodayDate = "Mars",
            monthlyIncomeInCents = 150000L,  // 1500.00€
            totalOutgoingsInCents = 180000L,  // 1800.00€
            disposableIncomeInCents = -30000L, // -300.00€ (Alerte rouge)
            remainingToPayInCents = 20000L,    // 200.00€
            onEditIncomeClick = { /* Action de test */ }
        )
    }
}