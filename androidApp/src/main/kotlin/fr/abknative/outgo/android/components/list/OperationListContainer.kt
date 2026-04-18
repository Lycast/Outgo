package fr.abknative.outgo.android.components.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.components.cards.GlassCard
import fr.abknative.outgo.android.designsystem.components.feedback.AppLoader
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.list.api.OperationFilter
import fr.abknative.outgo.wallet.api.model.presenter.ProjectedOperation

@Composable
fun OperationListContainer(
    isLoading: Boolean,
    filteredList: List<ProjectedOperation>,
    currentFilter: OperationFilter,
    onDeleteRequest: (ProjectedOperation) -> Unit,
    onEdit: (ProjectedOperation) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        when {
            isLoading -> item { AppLoader() }
            filteredList.isEmpty() -> item(key = "empty_state") { ListEmptyState(filter = currentFilter) }
            else -> {
                items(items = filteredList, key = { "${it.operation.id}_${it.projectedDate}" }) { projectedOp ->

                    Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

                    GlassCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        val displayOperation = projectedOp.operation.copy(
                            startDate = projectedOp.projectedDate
                        )

                        OperationCard(
                            operation = displayOperation,
                            formattedDate = projectedOp.formattedDate,
                            onEdit = { onEdit(projectedOp) },
                            onDeleteRequest = { onDeleteRequest(projectedOp) }
                        )
                    }
                }
            }
        }
    }
}