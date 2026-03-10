package fr.abknative.outgo.android.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.components.EmptyStateView
import fr.abknative.outgo.android.components.OutgoingCard
import fr.abknative.outgo.android.ui.CommonLabels
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
            .padding(horizontal = AppTheme.spacing.medium)
            .padding(top = AppTheme.spacing.small),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
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
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { dismissValue ->
                                if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                                    onDelete(outgoing.id)
                                    true
                                } else {
                                    false
                                }
                            },
                            positionalThreshold = { totalDistance -> totalDistance * 0.4f }
                        )

                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromStartToEnd = false,
                            backgroundContent = {
                                val isThresholdCrossed = dismissState.targetValue == SwipeToDismissBoxValue.EndToStart

                                val color by animateColorAsState(
                                    targetValue = if (isThresholdCrossed) MaterialTheme.colorScheme.errorContainer
                                    else MaterialTheme.colorScheme.surfaceVariant,
                                    label = "swipe_color_animation"
                                )

                                val scale by animateFloatAsState(
                                    targetValue = if (isThresholdCrossed) 1.2f else 1.0f,
                                    label = "swipe_scale_animation"
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(color)
                                        .padding(horizontal = AppTheme.spacing.large),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = CommonLabels.ACTION_DELETE,
                                        tint = if (isThresholdCrossed) MaterialTheme.colorScheme.onErrorContainer
                                        else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.scale(scale)
                                    )
                                }
                            },
                            content = {
                                OutgoingCard(
                                    outgoing = outgoing,
                                    onClick = { onEdit(outgoing) }
                                )
                            }
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    }
                }
            }
        }
    }
}