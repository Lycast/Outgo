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
import fr.abknative.outgo.android.ui.FormLabels
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor
import fr.abknative.outgo.wallet.api.model.operation.Recurrence

@Composable
fun OperationDateSelector(
    selectedDay: String,
    selectedRecurrence: Recurrence,
    onDayChanged: (String) -> Unit,
    onRecurrenceChanged: (Recurrence) -> Unit,
    modifier: Modifier = Modifier
) {
    val days = (1..31).map { it.toString() }

    // On mappe nos récurrences pour la deuxième roue
    val recurrences = listOf(
        Recurrence.UNIQUE,
        Recurrence.WEEKLY,
        Recurrence.MONTHLY,
        Recurrence.YEARLY
    )

    // On associe les labels UI (Tu pourras ajouter UNIQUE et WEEKLY dans FormLabels plus tard)
    val recurrenceLabels = recurrences.map { recurrence ->
        when (recurrence) {
            Recurrence.MONTHLY -> FormLabels.CYCLE_MONTHLY
            Recurrence.YEARLY -> FormLabels.CYCLE_YEARLY
            Recurrence.WEEKLY -> "Hebdomadaire"
            Recurrence.UNIQUE -> "Une fois"
            Recurrence.UNKNOWN -> ""
        }
    }

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
            selectedValue = selectedDay.ifEmpty { "1" }, // Sécurité si vide
            onSelectionChanged = onDayChanged,
            itemHeight = itemHeight,
            contentDescription = AccessibilityLabels.DAY_SELECTOR,
            dividerWidth = 0.4f,
            modifier = Modifier.weight(1f)
        )

        // Roue 2 : La Récurrence (Remplace l'ancienne roue des mois)
        WheelPicker(
            items = recurrences.map { it.name },
            itemLabels = recurrenceLabels,
            selectedValue = selectedRecurrence.name,
            onSelectionChanged = { selectedName ->
                onRecurrenceChanged(Recurrence.fromString(selectedName))
            },
            itemHeight = itemHeight,
            contentDescription = "Sélecteur de cycle",
            dividerWidth = 0.7f,
            modifier = Modifier.weight(1.5f)
        )
    }
}

// TON CODE INTACT : Aucune modification en dessous de cette ligne !
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