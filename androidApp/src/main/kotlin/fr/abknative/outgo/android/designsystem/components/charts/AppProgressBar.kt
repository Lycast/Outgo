package fr.abknative.outgo.android.designsystem.components.charts

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.OutgoTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor

/**
 * A versatile progress bar atom that can be oriented vertically or horizontally.
 *
 * @param progress Float between 0.0 and 1.0.
 * @param activeColor The color of the filled portion.
 * @param modifier The modifier to set dimensions and padding.
 * @param isVertical If true, fills from bottom to top. If false, fills from left to right.
 * @param trackColor The background color of the bar.
 * @param thickness The width (if vertical) or height (if horizontal) of the bar.
 */
@Composable
fun AppProgressBar(
    modifier: Modifier = Modifier,
    progress: Float,
    activeColor: Color,
    isVertical: Boolean = true,
    trackColor: Color = activeColor.copy(alpha = 0.1f),
    thickness: Dp = AppTheme.dimens.large,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 800),
        label = "progressBarAnimation"
    )

    // Dynamic shape based on orientation
    val barShape = if (isVertical) {
        RoundedCornerShape(
            topStart = AppTheme.dimens.small,
            topEnd = AppTheme.dimens.small,
            bottomStart = 0.dp,
            bottomEnd = 0.dp
        )
    } else {
        RoundedCornerShape(
            topStart = AppTheme.dimens.small,
            bottomStart = AppTheme.dimens.small,
            topEnd = AppTheme.dimens.small,
            bottomEnd = AppTheme.dimens.small
        )
    }

    Box(
        modifier = modifier
            .then(
                if (isVertical) Modifier.width(thickness)
                else Modifier.height(thickness)
            )
            .clip(barShape)
            .background(trackColor),
        contentAlignment = if (isVertical) Alignment.BottomCenter else Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .then(
                    if (isVertical) {
                        Modifier.fillMaxWidth().fillMaxHeight(animatedProgress)
                    } else {
                        Modifier.fillMaxHeight().fillMaxWidth(animatedProgress)
                    }
                )
                .background(activeColor)
        )
    }
}

// --- PREVIEWS ---

@Preview(showBackground = true, name = "Bar - Vertical Group")
@Composable
fun PreviewVerticalBarGroup() {
    OutgoTheme {
        Row(
            modifier = Modifier
                .height(200.dp)
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            // Here fillMaxHeight defines the total height of the chart track
            AppProgressBar(
                progress = 0.8f,
                activeColor = AppTheme.colors.primary.toColor(),
                modifier = Modifier.fillMaxHeight(),
                isVertical = true
            )
            AppProgressBar(
                progress = 0.5f,
                activeColor = AppTheme.colors.secondary.toColor(),
                modifier = Modifier.fillMaxHeight(),
                isVertical = true
            )
        }
    }
}

@Preview(showBackground = true, name = "Bar - Horizontal Single")
@Composable
fun PreviewHorizontalBarGroup() {
    OutgoTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // For horizontal, we usually want to fill the width
            AppProgressBar(
                progress = 0.8f,
                isVertical = false,
                activeColor = AppTheme.colors.primary.toColor(),
                modifier = Modifier.fillMaxWidth()
            )

            AppProgressBar(
                progress = 0.3f,
                isVertical = false,
                activeColor = AppTheme.colors.error.toColor(),
                modifier = Modifier.fillMaxWidth(),
                thickness = 8.dp // Testing a thinner bar
            )
        }
    }
}