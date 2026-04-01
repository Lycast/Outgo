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
import fr.abknative.outgo.wallet.api.model.Operation

@Composable
fun ExpenseListContainer(
    isLoading: Boolean,
    filteredList: List<Operation>,
    currentFilter: OperationFilter,
    onDelete: (String) -> Unit,
    onEdit: (Operation) -> Unit,
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
                items(items = filteredList, key = { it.id }) { outgoing ->

                    Card(
                        modifier = modifier.fillMaxWidth().padding(horizontal = AppTheme.spacing.medium),
                        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface100.toColor()),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f))
                    ) {
                        OutgoingCard(
                            operation = outgoing,
                            onEdit = { onEdit(outgoing) },
                            onDelete = { onDelete(outgoing.id) },
                            onDuplicate = {
                                onEdit(outgoing.copy(id = ""))
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(AppTheme.spacing.medium))
                }
            }
        }
    }
}