package fr.abknative.outgo.android.components.operation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.designsystem.atoms.CircleIcon
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.OutgoTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.DashboardLabels
import fr.abknative.outgo.android.ui.extensions.getUiColor
import fr.abknative.outgo.android.ui.extensions.uiAmount
import fr.abknative.outgo.android.ui.extensions.uiFrequencySummary
import fr.abknative.outgo.android.ui.extensions.uiTitle
import fr.abknative.outgo.core.api.model.SyncStatus
import fr.abknative.outgo.wallet.api.model.Operation
import fr.abknative.outgo.wallet.api.model.operation.OperationType
import fr.abknative.outgo.wallet.api.model.operation.Recurrence
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OperationCard(
    operation: Operation,
    onEdit: () -> Unit,
    onDeleteRequest: () -> Unit,
    modifier: Modifier = Modifier
) {

    val dateFormatter = remember { SimpleDateFormat("dd MMM", Locale.getDefault()) }
    val formattedDate = remember(operation.startDate) { dateFormatter.format(Date(operation.startDate)) }

    Column(
        modifier = modifier
            .fillMaxWidth()

            .combinedClickable(
                onClick = onEdit,
                onLongClick = onDeleteRequest
            )
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimens.large, vertical = AppTheme.dimens.large),
            verticalAlignment = Alignment.CenterVertically
        ) {

            val iconColor = operation.recurrence.getUiColor()

            CircleIcon(
                iconRes = R.drawable.credit_card_duotone,
                tint = iconColor
            )
            Spacer(modifier = Modifier.width(AppTheme.dimens.large))

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

                Spacer(modifier = Modifier.height(AppTheme.dimens.small))

                // --- Date et récurrence + Montant ---
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) {

                    // Date et récurrence
                    Row(horizontalArrangement = Arrangement.End) {
                        Text(
                            text = "${DashboardLabels.DUE_PREFIX} $formattedDate • ",
                            style = AppTheme.typo.caption,
                            color = AppTheme.colors.textSecondary.toColor()
                        )
                        Text(
                            text = operation.uiFrequencySummary,
                            style = AppTheme.typo.caption,
                            color = operation.recurrence.getUiColor()
                        )
                    }

                    Spacer(modifier = Modifier.width(AppTheme.dimens.medium))

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
    }
}

@Preview(showBackground = true, backgroundColor = 0x0000000)
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
            OperationCard(
                operation = mockOperation,
                onEdit = {},
                onDeleteRequest = {}
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
        OperationCard(
            operation = mockOperation,
            onEdit = {},
            onDeleteRequest = {}
        )
    }
}