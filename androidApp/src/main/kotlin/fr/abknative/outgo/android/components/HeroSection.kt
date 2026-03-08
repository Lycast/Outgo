package fr.abknative.outgo.android.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.ui.DashboardLabels
import fr.abknative.outgo.android.ui.uiAmount

@Composable
fun HeroSection(
    monthlyBudgetInCents: Long,
    remainingToPayThisMonthInCents: Long,
    onEditBudgetClick: () -> Unit,
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // --- Zone Budget (Cliquable) ---
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onEditBudgetClick() }
            ) {
                if (monthlyBudgetInCents <= 0L) {
                    Text(
                        text = DashboardLabels.HEADER_BUDGET_EMPTY,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        text = DashboardLabels.HEADER_BUDGET_PREFIX,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = monthlyBudgetInCents.uiAmount,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // --- Séparateur visuel discret ---
            Spacer(modifier = Modifier.height(8.dp))

            // --- Zone Reste à Payer ---
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = DashboardLabels.HERO_REMAINING_PREFIX,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = remainingToPayThisMonthInCents.uiAmount,
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}