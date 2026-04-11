package fr.abknative.outgo.android.components.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import fr.abknative.outgo.android.designsystem.components.cards.GlassCard
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.OutgoTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.DashboardLabels
import fr.abknative.outgo.dashboard.api.OperationFilter

/**
 * A tab-based selector to filter operations on the dashboard.
 * Uses individual [GlassCard] highlights for the selected state.
 *
 * @param selectedFilter The currently active [OperationFilter].
 * @param onFilterSelected Callback triggered when a new filter is tapped.
 * @param modifier The modifier to be applied to the row.
 */
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
            .padding(horizontal = AppTheme.dimens.medium)
            .padding(vertical = AppTheme.dimens.small),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.large)
    ) {
        FilterTabItem(
            modifier = Modifier.weight(1f),
            label = DashboardLabels.TAB_ALL,
            isSelected = selectedFilter == OperationFilter.ALL,
            onClick = { onFilterSelected(OperationFilter.ALL) }
        )

        FilterTabItem(
            modifier = Modifier.weight(1f),
            label = DashboardLabels.TAB_PAID,
            isSelected = selectedFilter == OperationFilter.PAST,
            onClick = { onFilterSelected(OperationFilter.PAST) }
        )

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

    val tabShape = AppTheme.shapes.large

    val tabContent = @Composable {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(tabShape)
                .clickable(onClick = onClick)
                .padding(vertical = AppTheme.dimens.medium),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label.uppercase(),
                style = AppTheme.typo.body,
                color = if (isSelected) {
                    AppTheme.colors.primary.toColor()
                } else {
                    AppTheme.colors.textSecondary.toColor()
                },
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
                tabContent()
            }
        } else {
            tabContent()
        }
    }
}

// --- PREVIEWS ---

@Preview(showBackground = true)
@Composable
fun PreviewOperationFilterSelector() {
    var currentFilter by remember { mutableStateOf(OperationFilter.ALL) }

    OutgoTheme {
        Box(modifier = Modifier.background(Color(0xFFE5E9F0)).padding(16.dp)) {
            OperationFilterSelector(
                selectedFilter = currentFilter,
                onFilterSelected = { currentFilter = it }
            )
        }
    }
}