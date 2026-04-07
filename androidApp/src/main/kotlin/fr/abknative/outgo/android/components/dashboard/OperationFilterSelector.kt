package fr.abknative.outgo.android.components.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.components.common.GlassCard
import fr.abknative.outgo.android.ui.DashboardLabels
import fr.abknative.outgo.android.ui.states.OperationFilter
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor

@Composable
fun OperationFilterSelector(
    selectedFilter: OperationFilter,
    onFilterSelected: (OperationFilter) -> Unit,
    modifier: Modifier = Modifier
) {

    val haptic = LocalHapticFeedback.current
    LaunchedEffect(selectedFilter) {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.spacing.medium)
            .padding(vertical = AppTheme.spacing.small),
        horizontalArrangement = Arrangement.Start
    ) {
        FilterTabItem(
            modifier = Modifier.weight(1f),
            label = DashboardLabels.TAB_ALL,
            isSelected = selectedFilter == OperationFilter.ALL,
            onClick = { onFilterSelected(OperationFilter.ALL) }
        )

        Spacer(modifier = Modifier.width(AppTheme.spacing.large))

        FilterTabItem(
            modifier = Modifier.weight(1f),
            label = DashboardLabels.TAB_PAID,
            isSelected = selectedFilter == OperationFilter.PAST,
            onClick = { onFilterSelected(OperationFilter.PAST) }
        )

        Spacer(modifier = Modifier.width(AppTheme.spacing.large))

        FilterTabItem(
            modifier = Modifier.weight(1f),
            label = DashboardLabels.TAB_REMAINING,
            isSelected = selectedFilter == OperationFilter.REMAINING,
            onClick = { onFilterSelected(OperationFilter.REMAINING) }
        )
    }
}

@Composable
private fun FilterTabItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tabShape = RoundedCornerShape(AppTheme.spacing.medium)

    val content = @Composable {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(tabShape)
                .clickable(onClick = onClick)
                .padding(vertical = AppTheme.spacing.medium),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label.uppercase(),
                style = AppTheme.typo.body,
                color = if (isSelected) AppTheme.colors.primary.toColor() else AppTheme.colors.textSecondary.toColor(),
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
            )
        }
    }

    Box(
        modifier = modifier
            .semantics {
                selected = isSelected
                role = Role.Tab
            }
    ) {
        if (isSelected) {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColorA = AppTheme.colors.surface200.toColor(),
            ) {
                content()
            }
        } else {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewExpenseFilterSelector() {
    var currentFilter by remember { mutableStateOf(OperationFilter.ALL) }

    Column(modifier = Modifier.background(Color(0xFFE5E9F0)).padding(16.dp)) { // Fond légèrement teinté pour voir le Glass
        OperationFilterSelector(
            selectedFilter = currentFilter,
            onFilterSelected = { currentFilter = it }
        )
    }
}