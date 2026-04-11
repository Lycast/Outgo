package fr.abknative.outgo.android.designsystem.foundation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import fr.abknative.outgo.android.R

/**
 * The foundational background for the entire application.
 * It combines a vertical gradient and a parametric pattern to create visual depth,
 * which is essential for highlighting the glassmorphism effect of overlaying components.
 *
 * @param modifier The modifier to be applied to the background container.
 * @param content The UI tree to be rendered on top of this background.
 */
@Composable
fun AppBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val primaryColor = AppTheme.colors.primary.toColor()
    val bgColor = AppTheme.colors.background.toColor()

    // Optimized: Cache the brush
    val backgroundBrush = remember(primaryColor, bgColor) {
        Brush.verticalGradient(
            colors = listOf(
                primaryColor.copy(alpha = 0.1f),
                bgColor
            )
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Layer 1: The Tinted Gradient
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(brush = backgroundBrush)
        )

        // Layer 2: The Structural Pattern
        Image(
            painter = painterResource(id = R.drawable.parametric_lines),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize(),
            alpha = 0.3f
        )

        // Layer 3: Screen Content
        content()
    }
}