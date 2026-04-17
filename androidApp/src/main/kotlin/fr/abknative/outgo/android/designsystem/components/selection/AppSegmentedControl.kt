package fr.abknative.outgo.android.designsystem.components.selection

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.foundation.AppText
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor

@Composable
fun AppSegmentedControl(
    items: List<String>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 48.dp,
    activeColor: Color = AppTheme.colors.primary.toColor(),
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(CircleShape)
            .background(AppTheme.colors.surface50.toColor().copy(alpha = 0.3f))
            .padding(AppTheme.dimens.extraSmall)
    ) {
        if (items.isEmpty()) return@BoxWithConstraints

        val segmentWidth = maxWidth / items.size

        val indicatorOffset by animateDpAsState(
            targetValue = segmentWidth * selectedIndex,
            animationSpec = spring(
                stiffness = Spring.StiffnessLow,
                dampingRatio = Spring.DampingRatioLowBouncy
            ),
            label = "indicatorOffset"
        )

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(segmentWidth)
                .offset(x = indicatorOffset)
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
                        style = AppTheme.typo.caption.copy(fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium),
                        color = if (isSelected) activeColor else AppTheme.colors.textSecondary.toColor()
                    )
                }
            }
        }
    }
}