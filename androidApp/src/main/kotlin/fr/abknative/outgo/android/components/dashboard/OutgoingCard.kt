package fr.abknative.outgo.android.components.dashboard

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.components.common.CircleIcon
import fr.abknative.outgo.android.ui.AccessibilityLabels
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.android.ui.DashboardLabels
import fr.abknative.outgo.android.ui.extensions.uiAmount
import fr.abknative.outgo.android.ui.extensions.uiFrequencySummary
import fr.abknative.outgo.android.ui.extensions.uiRecurrenceColor
import fr.abknative.outgo.android.ui.extensions.uiTitle
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.OutgoTheme
import fr.abknative.outgo.android.ui.theme.toColor
import fr.abknative.outgo.core.api.model.SyncStatus
import fr.abknative.outgo.wallet.api.model.Operation
import fr.abknative.outgo.wallet.api.model.operation.OperationType
import fr.abknative.outgo.wallet.api.model.operation.Recurrence
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OutgoingCard(
    operation: Operation,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onDuplicate: () -> Unit,
    modifier: Modifier = Modifier
) {

    var isExpanded by remember { mutableStateOf(false) }
    val stateDesc = if (isExpanded) AccessibilityLabels.COLLAPSE_DESC else AccessibilityLabels.EXPAND_DESC

    // Formatage dynamique de la date absolue
    val dateFormatter = remember { SimpleDateFormat("dd MMM", Locale.getDefault()) }
    val formattedDate = remember(operation.startDate) { dateFormatter.format(Date(operation.startDate)) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                stateDescription = stateDesc
                role = Role.Button
            }
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

            // Icon a gauche (On pourrait adapter la couleur selon INCOME ou EXPENSE plus tard)
            val iconColor = if (operation.type == OperationType.INCOME) AppTheme.colors.primary.toColor() else AppTheme.colors.secondary.toColor()
            val bgColor = if (operation.type == OperationType.INCOME) AppTheme.colors.surface100.toColor() else AppTheme.colors.surface200.toColor()

            CircleIcon(R.drawable.credit_card_duotone, iconColor, bgColor)
            Spacer(modifier = Modifier.width(AppTheme.spacing.large))

            Column(verticalArrangement = Arrangement.Center) {

                // --- Textes label ---
                Row( modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = operation.uiTitle,
                        style = AppTheme.typo.body,
                        color = AppTheme.colors.textPrimary.toColor(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(AppTheme.spacing.small))

                // --- Date et récurrence + Montant ---
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) {

                    // Date et récurrence
                    Row(horizontalArrangement = Arrangement.End) {
                        Text(
                            text = "${DashboardLabels.DUE_PREFIX} $formattedDate • ", // Remplacement de uiDueDayLabel
                            style = AppTheme.typo.caption,
                            color = AppTheme.colors.textSecondary.toColor()
                        )
                        Text(
                            text = operation.uiFrequencySummary,
                            style = AppTheme.typo.caption,
                            color = operation.recurrence.uiRecurrenceColor.copy(0.7f)
                        )
                    }

                    Spacer(modifier = Modifier.width(AppTheme.spacing.medium))

                    // Montant de la dépense
                    Text(
                        text = if(operation.type == OperationType.INCOME) "+ ${operation.amountInCents.uiAmount}" else operation.amountInCents.uiAmount,
                        style = AppTheme.typo.body,
                        color = if(operation.type == OperationType.INCOME) AppTheme.colors.primary.toColor() else AppTheme.colors.secondary.toColor(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // --- PARTIE 2 : Les actions (visibles uniquement si déplié) ---
        if (isExpanded) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = AppTheme.spacing.large),
                color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.05f)
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
                    colors = ButtonDefaults.textButtonColors(contentColor = AppTheme.colors.error.toColor())
                ) {
                    Icon(
                        painter = painterResource(R.drawable.trash),
                        contentDescription = AccessibilityLabels.DELETE_EXPENSE,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(AppTheme.spacing.extraSmall))
                    Text(
                        text = CommonLabels.ACTION_DELETE,
                        style = AppTheme.typo.caption,
                        color = AppTheme.colors.error.toColor(),
                    )
                }

                // Bouton Dupliquer
                TextButton(
                    onClick = {
                        isExpanded = false
                        onDuplicate()
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.copy),
                        contentDescription = AccessibilityLabels.DUPLICATE_EXPENSE,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(AppTheme.spacing.extraSmall))
                    Text(
                        text = CommonLabels.ACTION_DUPLICATE,
                        style = AppTheme.typo.caption,
                        color = AppTheme.colors.primary.toColor(),
                    )
                }

                // Bouton Éditer
                TextButton(
                    onClick = {
                        isExpanded = false
                        onEdit()
                    },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.pencil_simple),
                        contentDescription = AccessibilityLabels.EDIT_EXPENSE,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(AppTheme.spacing.extraSmall))
                    Text(
                        text = CommonLabels.ACTION_EDIT,
                        style = AppTheme.typo.caption,
                        color = AppTheme.colors.primary.toColor(),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOutgoingCard_LargeAmount() {
    val mockOperation = Operation(
        id = "1",
        walletId = "w1",
        name = "Achat d'une île privée au soleil",
        amountInCents = 1250000000000L, // 12,5 Milliard
        type = OperationType.EXPENSE,
        recurrence = Recurrence.YEARLY,
        startDate = 1718409600000L, // 15 Juin 2024
        endDate = null,
        syncStatus = SyncStatus.PENDING_CREATE,
        createdAt = 0L,
        updatedAt = 0L
    )

    OutgoTheme {
        Column(Modifier.padding(16.dp)) {
            OutgoingCard(
                operation = mockOperation,
                onEdit = {},
                onDelete = {},
                onDuplicate = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOutgoingCard_Expanded() {
    val mockOperation = Operation(
        id = "2",
        walletId = "w1",
        name = "Netflix",
        amountInCents = 1999L,
        type = OperationType.EXPENSE,
        recurrence = Recurrence.MONTHLY,
        startDate = 1717545600000L, // 5 Juin 2024
        endDate = null,
        syncStatus = SyncStatus.PENDING_UPDATE,
        createdAt = 0L,
        updatedAt = 0L
    )

    OutgoTheme {
        // On peut tester l'état déplié en cliquant dessus dans l'onglet "Interactive" d'Android Studio
        OutgoingCard(
            operation = mockOperation,
            onEdit = {},
            onDelete = {},
            onDuplicate = {}
        )
    }
}