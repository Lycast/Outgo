package fr.abknative.outgo.android.components.dashboard

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

            // --- TITRE ---
            MonthBudgetSelector(
                formattedTodayDate = formattedTodayDate,
                onEditIncomeClick = onEditIncomeClick
            )


            HorizontalDivider(
                modifier = Modifier.padding(horizontal = AppTheme.spacing.large),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f)
            )

            Spacer(modifier = Modifier.height(AppTheme.spacing.extraLarge))

            // --- BLOC 1 : Budget vs Reste à vivre ---
            PairedBudgetBar(
                topLabel = DashboardLabels.HERO_INCOME_LABEL,
                topAmount = monthlyIncomeInCents.uiAmount,
                topProgress = monthlyIncomeInCents / maxValue,
                topBarColor = AppTheme.dashboardColors.budget,

                bottomLabel = DashboardLabels.HERO_DISPOSABLE_INCOME_LABEL,
                bottomAmount = disposableIncomeInCents.uiAmount,
                bottomProgress = disposableRatio,
                bottomBarColor = liveColor
            )

            Spacer(modifier = Modifier.height(AppTheme.spacing.extraLarge))

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

            Spacer(modifier = Modifier.height(AppTheme.spacing.big))
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
    uiMode = Configuration.UI_MODE_NIGHT_YES
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