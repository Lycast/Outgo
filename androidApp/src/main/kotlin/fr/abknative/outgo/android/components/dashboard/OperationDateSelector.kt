package fr.abknative.outgo.android.components.dashboard

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.ui.AccessibilityLabels
import fr.abknative.outgo.android.ui.DashboardLabels
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor

@Composable
fun OperationDateSelector(
    selectedDay: String,
    selectedMonth: String,
    selectedYear: String,
    currentYear: Int,
    onDayChanged: (String) -> Unit,
    onMonthChanged: (String) -> Unit,
    onYearChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val days = (1..31).map { it.toString() }
    val months = listOf(
        "1" to DashboardLabels.MONTH_1,
        "2" to DashboardLabels.MONTH_2,
        "3" to DashboardLabels.MONTH_3,
        "4" to DashboardLabels.MONTH_4,
        "5" to DashboardLabels.MONTH_5,
        "6" to DashboardLabels.MONTH_6,
        "7" to DashboardLabels.MONTH_7,
        "8" to DashboardLabels.MONTH_8,
        "9" to DashboardLabels.MONTH_9,
        "10" to DashboardLabels.MONTH_10,
        "11" to DashboardLabels.MONTH_11,
        "12" to DashboardLabels.MONTH_12
    )
    val monthValues = months.map { it.first }
    val monthLabelsUI = months.map { it.second }
    val years = ((currentYear - 1)..(currentYear + 5)).map { it.toString() }

    val itemHeight = 40.dp
    val visibleItems = 3
    val containerHeight = itemHeight * visibleItems

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(containerHeight)
            .border(
                width = 1.dp,
                color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f),
                shape = RoundedCornerShape(4.dp)
            ),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Roue 1 : Le Jour
        WheelPicker(
            items = days,
            itemLabels = days,
            selectedValue = selectedDay.ifEmpty { "1" },
            onSelectionChanged = onDayChanged,
            itemHeight = itemHeight,
            contentDescription = AccessibilityLabels.DAY_SELECTOR, // Tu pourras ajuster les labels
            dividerWidth = 0.6f,
            modifier = Modifier.weight(1f)
        )

        // Roue 2 : Le Mois
        WheelPicker(
            items = monthValues,
            itemLabels = monthLabelsUI,
            selectedValue = selectedMonth.ifEmpty { "1" },
            onSelectionChanged = onMonthChanged,
            itemHeight = itemHeight,
            contentDescription = "Sélecteur de mois",
            dividerWidth = 0.8f,
            modifier = Modifier.weight(1.2f)
        )

        // Roue 3 : L'Année
        WheelPicker(
            items = years,
            itemLabels = years,
            selectedValue = selectedYear.ifEmpty { currentYear.toString() },
            onSelectionChanged = onYearChanged,
            itemHeight = itemHeight,
            contentDescription = "Sélecteur d'année",
            dividerWidth = 0.8f,
            modifier = Modifier.weight(1.2f)
        )
    }
}

@Composable
private fun WheelPicker(
    modifier: Modifier = Modifier,
    items: List<String>,
    itemLabels: List<String>,
    selectedValue: String,
    onSelectionChanged: (String) -> Unit,
    itemHeight: Dp,
    contentDescription: String,
    dividerWidth: Float = 1f
) {
    val listState = rememberLazyListState()
    val snapBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val initialIndex = items.indexOf(selectedValue).coerceAtLeast(0)

    val haptic = LocalHapticFeedback.current

    LaunchedEffect(initialIndex) { listState.scrollToItem(initialIndex) }

    val centerIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (visibleItemsInfo.isEmpty()) 0
            else {
                val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
                visibleItemsInfo.minByOrNull { item ->
                    kotlin.math.abs((item.offset + item.size / 2) - viewportCenter)
                }?.index ?: 0
            }
        }
    }

    LaunchedEffect(centerIndex) {
        if (listState.isScrollInProgress) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            if (centerIndex in items.indices && items[centerIndex] != selectedValue) {
                onSelectionChanged(items[centerIndex])
            }
        }
    }

    val currentLabelIndex = items.indexOf(selectedValue).coerceAtLeast(0)
    val currentSelectedLabel = itemLabels.getOrElse(currentLabelIndex) { "" }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .semantics {
                this.contentDescription = contentDescription
                this.stateDescription = currentSelectedLabel
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(itemHeight))

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(dividerWidth),
                thickness = 1.dp,
                color = AppTheme.colors.primary.toColor().copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.height(itemHeight))

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(dividerWidth),
                thickness = 1.dp,
                color = AppTheme.colors.primary.toColor().copy(alpha = 0.2f)
            )
        }

        LazyColumn(
            state = listState,
            flingBehavior = snapBehavior,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = itemHeight),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(items.size) { index ->
                val isSelected = index == centerIndex
                Box(
                    modifier = Modifier.fillMaxWidth().height(itemHeight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = itemLabels[index],
                        style = AppTheme.typo.body,
                        color = if (isSelected) AppTheme.colors.primary.toColor() else AppTheme.colors.textSecondary.toColor(),
                        modifier = Modifier
                            .alpha(if (isSelected) 1f else 0.4f)
                            .scale(if (isSelected) 1.15f else 1.0f)
                    )
                }
            }
        }
    }
}