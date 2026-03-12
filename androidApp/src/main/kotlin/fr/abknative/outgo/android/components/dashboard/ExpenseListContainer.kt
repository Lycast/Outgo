package fr.abknative.outgo.android.components.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.ui.components.LoaderItem
import fr.abknative.outgo.android.ui.states.OutgoingFilter
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.outgoing.api.model.Outgoing

@Composable
fun ExpenseListContainer(
    isLoading: Boolean,
    filteredList: List<Outgoing>,
    currentFilter: OutgoingFilter,
    onDelete: (String) -> Unit,
    onEdit: (Outgoing) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.spacing.medium),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f))
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
                        OutgoingCard(
                            outgoing = outgoing,
                            onEdit = { onEdit(outgoing) },
                            onDelete = { onDelete(outgoing.id) },
                            onDuplicate = {
                                onEdit(outgoing.copy(id = ""))
                            }
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = AppTheme.spacing.large),
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
                        )
                    }
                }
            }
        }
    }
}