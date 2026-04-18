package fr.abknative.outgo.android.components.list

/*
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
            AppText(
                text = label.uppercase(),
                color = if (isSelected) {
                    AppTheme.colors.primary.toColor()
                } else {
                    AppTheme.colors.textSecondary.toColor()
                },
                style = if (isSelected) AppTheme.typo.body else AppTheme.typo.caption
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
}*/