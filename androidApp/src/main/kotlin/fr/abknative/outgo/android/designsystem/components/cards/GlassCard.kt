package fr.abknative.outgo.android.designsystem.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor

/**
 * A custom glassmorphism card component with precise visual tuning.
 * Uses a multi-layer approach to handle shadows, glass gradient, and content sizing safely.
 * Optimized with [remember] to cache gradient brushes and prevent unnecessary allocations during recomposition.
 *
 * @param modifier The modifier to be applied to the outer boundaries of the card.
 * @param shape The shape of the card's corners.
 * @param backgroundColorA The primary tint color for the glass gradient.
 * @param content The composable content to be displayed inside the glass card.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = AppTheme.shapes.large,
    borderSize: Dp = AppTheme.dimens.small,
    elevation: Dp = 12.dp,
    backgroundColorA: Color = AppTheme.colors.surface200.toColor(),
    content: @Composable () -> Unit
) {
    val appBackgroundColor = AppTheme.colors.background.toColor()
    val textSecondaryColor = AppTheme.colors.textSecondary.toColor()
    val surface50Color = AppTheme.colors.surface50.toColor()

    // Optimized: Cached Brushes
    val backgroundBrush = remember(backgroundColorA, textSecondaryColor) {
        Brush.horizontalGradient(
            colors = listOf(textSecondaryColor.copy(alpha = 0.05f), backgroundColorA.copy(alpha = 0.8f))
        )
    }

    val borderBrush = remember(surface50Color) {
        Brush.verticalGradient(
            colors = listOf(surface50Color.copy(alpha = 0.5f), surface50Color.copy(alpha = 0.1f))
        )
    }

    Box(modifier = modifier) {

        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    this.shape = shape
                    shadowElevation = elevation.toPx()
                    clip = false
                }
                .background(
                    color = appBackgroundColor,
                    shape = shape
                )
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    this.shape = shape
                    clip = true
                }
                .background(brush = backgroundBrush)
                .border(
                    width = borderSize,
                    brush = borderBrush,
                    shape = shape
                )
        )
        content()
    }
}

