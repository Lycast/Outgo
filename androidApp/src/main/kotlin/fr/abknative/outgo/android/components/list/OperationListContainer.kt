package fr.abknative.outgo.android.components.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.components.operation.OperationCard
import fr.abknative.outgo.android.designsystem.components.cards.GlassCard
import fr.abknative.outgo.android.designsystem.components.feedback.AppLoader
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.list.api.OperationFilter
import fr.abknative.outgo.wallet.api.model.presenter.ProjectedOperation

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
        modifier = Modifier.fillMaxWidth().padding(horizontal = AppTheme.dimens.medium),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        when {
            isLoading -> item { AppLoader() }
            filteredList.isEmpty() -> item(key = "empty_state") { ListEmptyState(filter = currentFilter) }
            else -> {
                items(items = filteredList, key = { "${it.operation.id}_${it.projectedDate}" }) { projectedOp ->

                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColorA = AppTheme.colors.surface100.toColor(),

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

                    Spacer(modifier = Modifier.height(AppTheme.dimens.medium))
                }
            }
        }
    }
}