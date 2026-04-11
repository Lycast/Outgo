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
 * A minimalist vertical progress bar atom.
 * Fills from bottom to top with a smooth animation.
 *
 * @param progress Float between 0.0 and 1.0.
 * @param activeColor The color of the filled portion.
 * @param modifier The modifier to set height, width, and padding.
 * @param trackColor The background color of the bar (defaults to 10% activeColor).
 */
@Composable
fun AppVerticalBar(
    modifier: Modifier = Modifier,
    progress: Float,
    activeColor: Color,
    trackColor: Color = activeColor.copy(alpha = 0.1f),
    widthSize: Dp = AppTheme.dimens.large,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 800),
        label = "verticalBarAnimation"
    )

    val barShape = RoundedCornerShape(
        topStart = AppTheme.dimens.small,
        topEnd = AppTheme.dimens.small,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )

    Box(
        modifier = modifier
            .width(widthSize)
            .clip(barShape)
            .background(trackColor),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(animatedProgress)
                .background(activeColor)
        )
    }
}

// --- PREVIEWS ---

@Preview(showBackground = true, name = "Vertical Bar - Group")
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
            AppVerticalBar(
                progress = 0.8f,
                activeColor = AppTheme.colors.primary.toColor(),
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}