package fr.abknative.outgo.android.components.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    onDeleteRequest: (ProjectedOperation) -> Unit,
    onEdit: (ProjectedOperation) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        when {
            isLoading -> item { LoaderItem() }
            filteredList.isEmpty() -> item(key = "empty_state") { EmptyStateView(filter = currentFilter) }
            else -> {
                items(items = filteredList, key = { "${it.operation.id}_${it.projectedDate}" }) { projectedOp ->

                    Card(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = AppTheme.spacing.medium),
                        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface100.toColor()),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f))
                    ) {

                        val displayOperation = projectedOp.operation.copy(
                            startDate = projectedOp.projectedDate
                        )

                        OperationCard(
                            operation = displayOperation,
                            onEdit = { onEdit(projectedOp) },
                            onDeleteRequest = { onDeleteRequest(projectedOp) }

                        )
                    }

                    Spacer(modifier = Modifier.height(AppTheme.spacing.medium))
                }
            }
        }
    }
}