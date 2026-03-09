package fr.abknative.outgo.android.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.ui.DashboardLabels
import fr.abknative.outgo.android.ui.uiAmount

@Composable
fun HeroSection(
    formattedTodayDate: String,
    monthlyIncomeInCents: Long,
    totalOutgoingsInCents: Long,
    disposableIncomeInCents: Long,
    remainingToPayInCents: Long,
    onEditIncomeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // --- Date du jour ---
            Text(
                text = "${DashboardLabels.HERO_DATE_PREFIX} $formattedTodayDate",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
            )

            // --- Zone Revenu ---
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onEditIncomeClick() }
            ) {
                Text(
                    text = DashboardLabels.HERO_INCOME_LABEL,
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = if (monthlyIncomeInCents <= 0L) DashboardLabels.HERO_BUDGET_EMPTY
                    else monthlyIncomeInCents.uiAmount,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            // --- Chiffre IMPORTANT : Reste à vivre ---
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = DashboardLabels.HERO_DISPOSABLE_INCOME_LABEL,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = disposableIncomeInCents.uiAmount,
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Black,
                    // Si le reste à vivre est négatif, on alerte visuellement
                    color = if (disposableIncomeInCents < 0) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f)
            )

            // --- Détails secondaires ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                InfoColumn(
                    label = DashboardLabels.HERO_TOTAL_CHARGES_LABEL,
                    value = totalOutgoingsInCents.uiAmount
                )

                InfoColumn(
                    label = DashboardLabels.HERO_REMAINING_TO_PAY_LABEL,
                    value = remainingToPayInCents.uiAmount,
                    isImportant = true
                )
            }
        }
    }
}

@Composable
private fun InfoColumn(label: String, value: String, isImportant: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelSmall)
        Text(
            text = value,
            style = if (isImportant) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium,
            fontWeight = if (isImportant) FontWeight.Bold else FontWeight.Normal
        )
    }
}