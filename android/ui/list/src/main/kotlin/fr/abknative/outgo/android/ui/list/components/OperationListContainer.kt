package fr.abknative.outgo.android.ui.list.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.core.ListLabels
import fr.abknative.outgo.android.core.components.cards.GlassCard
import fr.abknative.outgo.android.core.components.cards.OperationCard
import fr.abknative.outgo.android.core.designsystem.AppText
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor
import fr.abknative.outgo.android.core.extensions.uiAmount
import fr.abknative.outgo.android.ui.list.getUiColor
import fr.abknative.outgo.android.ui.list.uiFormattedLabel
import fr.abknative.outgo.android.ui.list.uiLabel
import fr.abknative.outgo.android.ui.list.uiTitle
import fr.abknative.outgo.list.api.ListViewMode
import fr.abknative.outgo.wallet.api.model.operation.OperationType
import fr.abknative.outgo.wallet.api.model.operation.Recurrence
import fr.abknative.outgo.wallet.api.model.presenter.ProjectedOperation

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OperationListContainer(
    isLoading: Boolean,
    viewMode: ListViewMode,
    currentDateMillis: Long,
    groupedOperations: Map<String, List<ProjectedOperation>>,
    onDeleteRequest: (ProjectedOperation) -> Unit,
    onUnsubscribe: (ProjectedOperation) -> Unit,
    onEdit: (ProjectedOperation) -> Unit,
    onDuplicate: (ProjectedOperation) -> Unit,
    modifier: Modifier = Modifier
) {

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
        return
    }

    if (groupedOperations.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize().padding(AppTheme.dimens.extraLarge),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.medium)
            ) {
                AppText(
                    text = ListLabels.EMPTY_ALL,
                    style = AppTheme.typo.body.copy(fontWeight = FontWeight.Bold),
                    color = AppTheme.colors.textPrimary.toColor(),
                    textAlign = TextAlign.Center
                )

                AppText(
                    text = ListLabels.EMPTY_STATE_DESC,
                    style = AppTheme.typo.caption,
                    color = AppTheme.colors.textSecondary.toColor(),
                    textAlign = TextAlign.Center
                )
            }
        }
        return
    }

    var expandedCardKey by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = modifier.fillMaxWidth().padding(horizontal = AppTheme.dimens.medium),
        contentPadding = PaddingValues(bottom = 80.dp, top = AppTheme.dimens.medium)
    ) {
        val entries = groupedOperations.entries.toList()

        entries.forEachIndexed { index, (headerTitle, operations) ->

            if (headerTitle != "GLOBAL_RULES") {
                item(key = "header_$headerTitle") {
                    AppText(
                        text = "${ListLabels.DUE_PREFIX} $headerTitle",
                        style = AppTheme.typo.caption,
                        color = AppTheme.colors.primary.toColor(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(horizontal = AppTheme.dimens.large)
                            .padding(top = if (index == 0) 0.dp else AppTheme.dimens.large)
                    )
                }
            }

            items(
                items = operations,
                key = { "${it.operation.id}_${it.projectedDate}" }
            ) { projectedOp ->

                val currentItemKey = "${projectedOp.operation.id}_${projectedOp.projectedDate}"
                val op = projectedOp.operation
                val isClosable = op.recurrence != Recurrence.UNIQUE && (op.endDate == null || op.endDate!! > currentDateMillis)

                if (viewMode == ListViewMode.PROJECTED) Spacer(modifier = Modifier.height(AppTheme.dimens.extraSmall))
                else Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

                GlassCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val bottomLeftText = when (viewMode) {
                        ListViewMode.PROJECTED -> { op.recurrence.uiFormattedLabel }
                        ListViewMode.STANDARD -> {
                            if (projectedOp.formattedEndDate.isNotEmpty()) {
                                "${ListLabels.FROM_PREFIX} ${projectedOp.formattedStartDate} ${ListLabels.TO_PREFIX} ${projectedOp.formattedEndDate}"
                            } else { "${ListLabels.SINCE_PREFIX} ${projectedOp.formattedStartDate}" }
                        }
                    }

                    val topRightText = when (viewMode) {
                        ListViewMode.PROJECTED -> { null }
                        ListViewMode.STANDARD -> { projectedOp.operation.recurrence.uiLabel }
                    }

                    OperationCard(
                        topLeftText = op.uiTitle,
                        topRightText = topRightText,
                        bottomLeftText = bottomLeftText,
                        bottomRightText = op.amountInCents.uiAmount,
                        amountColor = if (op.type == OperationType.INCOME) AppTheme.colors.primary.toColor() else AppTheme.colors.textPrimary.toColor(),
                        iconColor = op.recurrence.getUiColor().copy(alpha = 0.7f),
                        isExpanded = expandedCardKey == currentItemKey,
                        onToggleExpand = {
                            expandedCardKey = if (expandedCardKey == currentItemKey) null else currentItemKey
                        },
                        onDeleteRequest = { onDeleteRequest(projectedOp) },
                        onUnsubscribeRequest = if (isClosable) {
                            { onUnsubscribe(projectedOp) }
                        } else null,
                        onEditRequest = { onEdit(projectedOp) },
                        onDuplicateRequest = { onDuplicate(projectedOp) }
                    )
                }
            }
        }
    }
}