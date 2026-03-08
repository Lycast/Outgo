package fr.abknative.outgo.android.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.ui.DashboardLabels
import fr.abknative.outgo.android.ui.states.ExpenseFilterType

@Composable
fun ExpenseFilterSelector(
    selectedFilter: ExpenseFilterType,
    onFilterSelected: (ExpenseFilterType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedFilter == ExpenseFilterType.ALL,
            onClick = { onFilterSelected(ExpenseFilterType.ALL) },
            label = { Text(DashboardLabels.TAB_ALL) }
        )

        FilterChip(
            selected = selectedFilter == ExpenseFilterType.PAID,
            onClick = { onFilterSelected(ExpenseFilterType.PAID) },
            label = { Text(DashboardLabels.TAB_PAID) }
        )

        FilterChip(
            selected = selectedFilter == ExpenseFilterType.REMAINING,
            onClick = { onFilterSelected(ExpenseFilterType.REMAINING) },
            label = { Text(DashboardLabels.TAB_REMAINING) }
        )
    }
}