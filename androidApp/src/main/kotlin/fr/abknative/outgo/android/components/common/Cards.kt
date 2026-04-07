package fr.abknative.outgo.android.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor

/**
 * A custom glassmorphism card component with precise visual tuning.
 * Uses a multi-layer approach to handle shadows, glass gradient, and content sizing safely.
 * Optimized with [remember] to cache gradient brushes and prevent unnecessary allocations during recomposition.
 *
 * @param modifier The modifier to be applied to the outer boundaries of the card.
 * @param corner The shape of the card's corners.
 * @param backgroundColorA The primary tint color for the glass gradient.
 * @param content The composable content to be displayed inside the glass card.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    corner: RoundedCornerShape = RoundedCornerShape(AppTheme.spacing.large),
    backgroundColorA: Color = AppTheme.colors.surface50.toColor(),
    content: @Composable () -> Unit
) {
    val appBackgroundColor = AppTheme.colors.background.toColor()
    val textSecondaryColor = AppTheme.colors.textSecondary.toColor()
    val surface50Color = AppTheme.colors.surface50.toColor()

    val backgroundBrush = remember(backgroundColorA, textSecondaryColor) {
        Brush.horizontalGradient(
            colors = listOf(
                textSecondaryColor.copy(alpha = 0.05f),
                backgroundColorA.copy(alpha = 0.8f)
            )
        )
    }

    val borderBrush = remember(surface50Color) {
        Brush.verticalGradient(
            colors = listOf(
                surface50Color.copy(alpha = 0.5f),
                surface50Color.copy(alpha = 0.1f)
            )
        )
    }

    Box(modifier = modifier) {

        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    shape = corner
                    shadowElevation = 12f
                    clip = false
                }
                .background(
                    color = appBackgroundColor,
                    shape = corner
                )
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    shape = corner
                    clip = true
                }
                .background(brush = backgroundBrush)
                .border(
                    width = AppTheme.spacing.small,
                    brush = borderBrush,
                    shape = corner
                )
        )
        content()
    }
}

/**
 * Applies a glassmorphism visual effect to the component.
 * Uses [composed] to safely access the Compose theme within the modifier chain.
 *
 * @param shape The shape of the glass effect boundaries (e.g., RoundedCornerShape).
 * @param borderSize The thickness of the gradient border.
 */
fun Modifier.glassEffect(
    shape: Shape,
    borderSize: Dp
): Modifier = composed {

    val colorA = AppTheme.colors.textPrimary.toColor()
    val colorB = AppTheme.colors.textSecondary.toColor()
    val borderColor = AppTheme.colors.surface50.toColor()

    val backgroundBrush = remember(colorA, colorB) {
        Brush.horizontalGradient(
            colors = listOf(
                colorB.copy(alpha = 0.02f),
                colorA.copy(alpha = 0.05f)
            )
        )
    }

    val borderBrush = remember(borderColor) {
        Brush.verticalGradient(
            colors = listOf(
                borderColor,
                borderColor.copy(alpha = 0.5f)
            )
        )
    }


    this.then(
        Modifier
            .clip(shape)
            .background(brush = backgroundBrush)
            .border(
                width = borderSize,
                brush = borderBrush,
                shape = shape
            )
    )
}