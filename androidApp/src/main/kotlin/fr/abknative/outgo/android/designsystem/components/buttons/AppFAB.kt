package fr.abknative.outgo.android.designsystem.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.OutgoTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.AccessibilityLabels

/**
 * A custom action trigger designed with a multi-layer approach.
 * Uses an isolation layer for clean shadow rendering and a top layer
 * for the primary color and border lighting.
 *
 * @param onClick Callback to be invoked when the button is pressed.
 * @param modifier Modifier to be applied to the outer container.
 */
@Composable
fun AppFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconRes: Int = R.drawable.plus_bold,
    size: Dp = 58.dp,
    outerShape: Shape = AppTheme.shapes.large,
    innerShape: Shape = AppTheme.shapes.medium,
) {

    val primaryColor = AppTheme.colors.primary.toColor()
    val textSecondaryColor = AppTheme.colors.textSecondary.toColor()
    val backgroundColor = AppTheme.colors.background.toColor()

    // Optimized: Cached Brushes
    val borderBrush = remember(textSecondaryColor) {
        Brush.verticalGradient(
            colors = listOf(textSecondaryColor.copy(alpha = 0.1f), textSecondaryColor.copy(alpha = 0.15f))
        )
    }

    val primaryBrush = remember(primaryColor) {
        Brush.horizontalGradient(
            colors = listOf(primaryColor, primaryColor.copy(alpha = 0.9f))
        )
    }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        // Layer 1: Isolation & Shadow
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    shape = outerShape
                    shadowElevation = 12.dp.toPx()
                    clip = false
                }
                .background(color = backgroundColor, shape = outerShape)
        )

        // Layer 2: Border / Frame
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(brush = borderBrush, shape = outerShape)
        )

        // Layer 3: Main Action Layer
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(AppTheme.dimens.medium)
                .clip(innerShape)
                .background(brush = primaryBrush)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = AccessibilityLabels.ADD_EXPENSE,
                tint = AppTheme.colors.textOnBrand.toColor(),
                modifier = Modifier.size(AppTheme.dimens.large)
            )
        }
    }
}


/**
 * Preview for the AddActionTrigger component.
 * Wrapped in [AppTheme] to provide the required CompositionLocal color tokens.
 */
@Preview(showBackground = true, name = "AddActionTrigger - Glass Effect")
@Composable
fun PreviewAddActionTrigger() {
    OutgoTheme {
        Box(
            modifier = Modifier
                .background(AppTheme.colors.background.toColor())
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            AppFAB(
                onClick = { /* Preview mock action */ }
            )
        }
    }
}