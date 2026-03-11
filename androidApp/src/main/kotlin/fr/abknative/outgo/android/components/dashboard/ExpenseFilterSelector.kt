package fr.abknative.outgo.android.components.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.ui.DashboardLabels
import fr.abknative.outgo.android.ui.states.OutgoingFilter
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.OutgoTheme

@Composable
fun ExpenseFilterSelector(
    selectedFilter: OutgoingFilter,
    onFilterSelected: (OutgoingFilter) -> Unit,
    modifier: Modifier = Modifier
) {

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.spacing.medium),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp,MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)),
        shape = MaterialTheme.shapes.medium
    ) {
        // FILTER TAB ROW
        Row(
            modifier = Modifier.padding(AppTheme.spacing.small),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Filtre TOUT
            FilterTabItem(
                label = DashboardLabels.TAB_ALL,
                isSelected = selectedFilter == OutgoingFilter.ALL,
                onClick = { onFilterSelected(OutgoingFilter.ALL) },
                modifier = Modifier.weight(1f)
            )

            // Filtre PAYÉS
            FilterTabItem(
                label = DashboardLabels.TAB_PAID,
                isSelected = selectedFilter == OutgoingFilter.PAID,
                onClick = { onFilterSelected(OutgoingFilter.PAID) },
                modifier = Modifier.weight(1f)
            )

            // Filtre RESTANT
            FilterTabItem(
                label = DashboardLabels.TAB_REMAINING,
                isSelected = selectedFilter == OutgoingFilter.REMAINING,
                onClick = { onFilterSelected(OutgoingFilter.REMAINING) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun FilterTabItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(36.dp),
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
        border = if (isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)) else null,
        contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.primary
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
            )
        }
    }
}

@Preview(showBackground = true, name = "Filtres - État Sélectionné")
@Composable
fun PreviewExpenseFilterSelector() {
    var currentFilter by remember { mutableStateOf(OutgoingFilter.ALL) }

    OutgoTheme {
        ExpenseFilterSelector(
            selectedFilter = currentFilter,
            onFilterSelected = { currentFilter = it }
        )
    }
}