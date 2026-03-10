package fr.abknative.outgo.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.ui.*
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.OutgoTheme
import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.outgoing.api.Recurrence
import fr.abknative.outgo.outgoing.api.model.Outgoing

@Composable
fun OutgoingCard(
    outgoing: Outgoing,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val isSyncIndicatorVisible = outgoing.syncStatus != SyncStatus.SYNCED

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface) // Fond blanc unifié
            .clickable(onClick = onClick) // Remplacement de l'action onClick de la Card
            .padding(horizontal = AppTheme.spacing.large, vertical = AppTheme.spacing.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- 1. ZONE GAUCHE : L'Ancre Visuelle ---
        CircleIcon(Icons.Rounded.CreditCard)

        Spacer(modifier = Modifier.width(AppTheme.spacing.large))

        // --- 2. ZONE CENTRE : Les Informations (Titre + Détails) ---
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = outgoing.uiTitle,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(AppTheme.spacing.extraSmall))

            Text(
                text = "${outgoing.uiDueDayLabel} • ${outgoing.uiFrequencySummary}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.width(AppTheme.spacing.small))

        // --- 3. ZONE DROITE : Le Quantitatif (Montant) ---
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = outgoing.amountInCents.uiAmount,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Pastille de couleur pour le SyncStatus
            if (isSyncIndicatorVisible) {
                Spacer(modifier = Modifier.width(AppTheme.spacing.small))
            } else {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(outgoing.syncStatus.uiColor)
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Liste de Dépenses")
@Composable
fun PreviewOutgoingCardList() {
    // Création de données fictives
    val mockOutgoingSynced = Outgoing(
        id = "1",
        name = "Abonnement Netflix",
        amountInCents = 1599L,
        dueDay = 5,
        syncStatus = SyncStatus.SYNCED,
        recurrence = Recurrence.MONTHLY,
        createdAt = 0L,
        updatedAt = 0L
    )

    val mockOutgoingPending = Outgoing(
        id = "2",
        name = "Loyer Mars",
        amountInCents = 85000L,
        dueDay = 1,
        syncStatus = SyncStatus.SYNCED,
        recurrence = Recurrence.MONTHLY,
        createdAt = 0L,
        updatedAt = 0L
    )

    OutgoTheme {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                OutgoingCard(
                    outgoing = mockOutgoingSynced,
                    onClick = {}
                )
            }
            item {
                OutgoingCard(
                    outgoing = mockOutgoingPending,
                    onClick = {}
                )
            }
        }
    }
}