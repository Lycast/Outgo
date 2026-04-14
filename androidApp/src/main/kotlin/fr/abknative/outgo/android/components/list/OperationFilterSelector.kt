package fr.abknative.outgo.android.components.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import fr.abknative.outgo.android.designsystem.components.cards.GlassCard
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.ListLabels
import fr.abknative.outgo.list.api.OperationFilter

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
            .padding(vertical = AppTheme.dimens.medium),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.large)
    ) {
        FilterTabItem(
            modifier = Modifier.weight(1f),
            label = ListLabels.TAB_ALL,
            isSelected = selectedFilter == OperationFilter.ALL,
            onClick = { onFilterSelected(OperationFilter.ALL) }
        )

        FilterTabItem(
            modifier = Modifier.weight(1f),
            label = ListLabels.TAB_PAID,
            isSelected = selectedFilter == OperationFilter.PAST,
            onClick = { onFilterSelected(OperationFilter.PAST) }
        )

        FilterTabItem(
            modifier = Modifier.weight(1f),
            label = ListLabels.TAB_REMAINING,
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