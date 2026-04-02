package fr.abknative.outgo.android.components.common

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor

/**
 * A highly customizable, animated segmented control.
 * Displays a pill-shaped container with a sliding selection indicator.
 *
 * @param items The list of text labels to display for each segment.
 * @param selectedIndex The index of the currently selected segment.
 * @param onItemSelected Callback triggered when a segment is tapped.
 * @param activeColor The color used for the selection indicator and active text.
 * @param modifier The modifier to be applied to the outer layout.
 */
@Composable
fun SegmentedControl(
    items: List<String>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    activeColor: Color = AppTheme.colors.primary.toColor(),
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(CircleShape)
            .background(AppTheme.colors.surface100.toColor())
            .padding(4.dp) // Espace interne pour que la pastille respire
    ) {
        val segmentWidth = maxWidth / items.size

        // Animation fluide de la pastille
        val indicatorOffset by animateDpAsState(
            targetValue = segmentWidth * selectedIndex,
            animationSpec = spring(stiffness = Spring.StiffnessLow),
            label = "indicatorOffset"
        )

        // L'indicateur visuel (la pastille qui bouge)
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(segmentWidth)
                .offset(x = indicatorOffset)
                .clip(CircleShape)
                .background(activeColor.copy(alpha = 0.15f))
        )

        // Les textes cliquables (au premier plan)
        Row(modifier = Modifier.fillMaxSize()) {
            items.forEachIndexed { index, label ->
                val isSelected = index == selectedIndex

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null, // Enlève l'effet "ripple" pour un look plus natif
                            role = Role.RadioButton,
                            onClick = { onItemSelected(index) }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        style = AppTheme.typo.caption,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) activeColor else AppTheme.colors.textSecondary.toColor()
                    )
                }
            }
        }
    }
}