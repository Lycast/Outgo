package fr.abknative.outgo.android.components.dashboard

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.components.common.CircleIcon
import fr.abknative.outgo.android.ui.*
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.core.api.SyncStatus
import fr.abknative.outgo.outgoing.api.model.Outgoing

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OutgoingCard(
    outgoing: Outgoing,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onDuplicate: () -> Unit,
    modifier: Modifier = Modifier
) {

    val isSyncIndicatorVisible = outgoing.syncStatus != SyncStatus.SYNCED
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .combinedClickable(
                onClick = { isExpanded = !isExpanded },
                onLongClick = {
                    isExpanded = false
                    onEdit()
                }
            )
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
    ) {

        // --- PARTIE 1 : Le contenu toujours visible ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.spacing.large, vertical = AppTheme.spacing.large),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleIcon(Icons.Rounded.CreditCard)
            Spacer(modifier = Modifier.width(AppTheme.spacing.large))

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
                Row {
                    Text(
                        text = "${outgoing.uiDueDayLabel} • ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                    Text(
                        text = outgoing.uiFrequencySummary,
                        style = MaterialTheme.typography.bodySmall,
                        color = outgoing.recurrence.uiRecurrenceColor.copy(0.7f),
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.width(AppTheme.spacing.small))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = outgoing.amountInCents.uiAmount,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
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

        // --- PARTIE 2 : Les actions (visibles uniquement si déplié) ---
        if (isExpanded) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = AppTheme.spacing.large),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.05f)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.spacing.large, vertical = AppTheme.spacing.extraSmall),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Bouton Supprimer
                TextButton(
                    onClick = {
                        isExpanded = false
                        onDelete()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = AccessibilityLabels.DELETE_EXPENSE, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(AppTheme.spacing.extraSmall))
                    Text(CommonLabels.ACTION_DELETE)
                }

                // Bouton Dupliquer
                TextButton(
                    onClick = {
                        isExpanded = false
                        onDuplicate()
                    }
                ) {
                    Icon(
                        Icons.Default.ContentCopy,
                        contentDescription = AccessibilityLabels.DUPLICATE_EXPENSE,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(AppTheme.spacing.extraSmall))
                    Text(CommonLabels.ACTION_DUPLICATE)
                }

                // Bouton Éditer
                TextButton(
                    onClick = {
                        isExpanded = false
                        onEdit()
                    },
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = AccessibilityLabels.EDIT_EXPENSE,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(AppTheme.spacing.extraSmall))
                    Text(CommonLabels.ACTION_EDIT)
                }
            }
        }
    }
}