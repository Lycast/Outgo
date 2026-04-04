package fr.abknative.outgo.android.components.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.components.common.ConfirmationDialog
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.android.ui.DialogLabels
import fr.abknative.outgo.android.ui.components.LoaderItem
import fr.abknative.outgo.android.ui.states.OperationFilter
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor
import fr.abknative.outgo.wallet.api.model.dashboard.ProjectedOperation

@Composable
fun OperationListContainer(
    isLoading: Boolean,
    filteredList: List<ProjectedOperation>,
    currentFilter: OperationFilter,
    onDeleteRequest: (String) -> Unit,
    onEdit: (ProjectedOperation) -> Unit,
    modifier: Modifier = Modifier
) {

    var expandedCardKey by remember { mutableStateOf<String?>(null) }
    var operationToDelete by remember { mutableStateOf<ProjectedOperation?>(null) }


    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        when {
            isLoading -> item { LoaderItem() }
            filteredList.isEmpty() -> item(key = "empty_state") { EmptyStateView(filter = currentFilter) }
            else -> {
                items(items = filteredList, key = { "${it.operation.id}_${it.projectedDate}" }) { projectedOp ->

                    val itemKey = "${projectedOp.operation.id}_${projectedOp.projectedDate}"
                    val isExpanded = expandedCardKey == itemKey

                    Card(
                        modifier = modifier.fillMaxWidth().padding(horizontal = AppTheme.spacing.medium),
                        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface100.toColor()),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f))
                    ) {

                        val displayOperation = projectedOp.operation.copy(
                            startDate = projectedOp.projectedDate
                        )

                        OperationCard(
                            operation = displayOperation,
                            isExpanded = isExpanded,
                            onToggleExpand = {
                                expandedCardKey = if (isExpanded) null else itemKey
                            },
                            onEdit = { onEdit(projectedOp) },
                            onDeleteRequest = {
                                expandedCardKey = null
                                operationToDelete = projectedOp
                           },
                            onDuplicate = {
                                val duplicatedOp = projectedOp.copy(
                                    operation = projectedOp.operation.copy(id = "")
                                )
                                onEdit(duplicatedOp)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(AppTheme.spacing.medium))
                }
            }
        }
    }

    if (operationToDelete != null) {
        ConfirmationDialog(
            title = DialogLabels.DELETE_OPERATION_TITLE,
            description = DialogLabels.DELETE_OPERATION_DESC,
            confirmLabel = CommonLabels.ACTION_DELETE,
            cancelLabel = CommonLabels.ACTION_CANCEL,
            isDestructive = true,
            onConfirm = {
                operationToDelete?.let { onDeleteRequest(it.operation.id) }
                operationToDelete = null
            },
            onDismiss = { operationToDelete = null }
        )
    }
}