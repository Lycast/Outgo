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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.ui.DashboardLabels
import fr.abknative.outgo.android.ui.states.OutgoingFilter
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor

@Composable
fun ExpenseFilterSelector(
    selectedFilter: OutgoingFilter,
    onFilterSelected: (OutgoingFilter) -> Unit,
    modifier: Modifier = Modifier
) {

    val haptic = LocalHapticFeedback.current
    LaunchedEffect(selectedFilter) {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.spacing.large),
        horizontalArrangement = Arrangement.Start
    ) {
        FilterTabItem(
            modifier = Modifier.weight(1f),
            label = DashboardLabels.TAB_ALL,
            isSelected = selectedFilter == OutgoingFilter.ALL,
            onClick = { onFilterSelected(OutgoingFilter.ALL) }
        )

        Spacer(modifier = Modifier.width(AppTheme.spacing.large))

        FilterTabItem(
            modifier = Modifier.weight(1f),
            label = DashboardLabels.TAB_PAID,
            isSelected = selectedFilter == OutgoingFilter.PAID,
            onClick = { onFilterSelected(OutgoingFilter.PAID) }
        )

        Spacer(modifier = Modifier.width(AppTheme.spacing.large))

        FilterTabItem(
            modifier = Modifier.weight(1f),
            label = DashboardLabels.TAB_REMAINING,
            isSelected = selectedFilter == OutgoingFilter.REMAINING,
            onClick = { onFilterSelected(OutgoingFilter.REMAINING) }
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
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(AppTheme.spacing.small))
            .clickable(onClick = onClick)
            .padding(vertical = AppTheme.spacing.small),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label.uppercase(),
            style = AppTheme.typo.body,
            color = if (isSelected) AppTheme.colors.primary.toColor() else AppTheme.colors.textSecondary.toColor(),
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .height(3.dp)
                .fillMaxWidth(if (isSelected) 1f else 0f)
                .background(
                    color = if (isSelected) AppTheme.colors.primary.toColor() else Color.Transparent,
                    shape = RoundedCornerShape(percent = 50)
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewExpenseFilterSelector() {
    var currentFilter by remember { mutableStateOf(OutgoingFilter.ALL) }

    Column(modifier = Modifier.background(Color.White).padding(16.dp)) {
        ExpenseFilterSelector(
            selectedFilter = currentFilter,
            onFilterSelected = { currentFilter = it }
        )
    }
}