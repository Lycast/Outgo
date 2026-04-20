package fr.abknative.outgo.android.core.components.selection

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.core.designsystem.AppText
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor

/**
 * A customized segmented control component.
 * Displays a horizontal list of options with a smooth animated selection indicator.
 */
@Composable
fun AppSegmentedControl(
    items: List<String>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 48.dp,
    activeColor: Color = AppTheme.colors.primary.toColor(),
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(CircleShape)
            .background(AppTheme.colors.surface50.toColor().copy(alpha = 0.3f))
            .padding(AppTheme.dimens.extraSmall)
    ) {
        if (items.isEmpty()) return

        val animatedIndex by animateFloatAsState(
            targetValue = selectedIndex.toFloat(),
            animationSpec = spring(
                stiffness = Spring.StiffnessLow,
                dampingRatio = Spring.DampingRatioLowBouncy
            ),
            label = "indicatorAnimation"
        )

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(1f / items.size)
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    val xOffset = (placeable.width * animatedIndex).toInt()
                    layout(placeable.width, placeable.height) {
                        placeable.placeRelative(xOffset, 0)
                    }
                }
                .clip(CircleShape)
                .background(activeColor.copy(alpha = 0.15f))
        )

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
                            indication = null,
                            role = Role.RadioButton,
                            onClick = { onItemSelected(index) }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    AppText(
                        text = label,
                        style = AppTheme.typo.caption.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        ),
                        color = if (isSelected) AppTheme.colors.textOnBrand.toColor() else AppTheme.colors.textSecondary.toColor()
                    )
                }
            }
        }
    }
}