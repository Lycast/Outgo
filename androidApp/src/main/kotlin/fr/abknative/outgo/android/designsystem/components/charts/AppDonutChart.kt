package fr.abknative.outgo.android.designsystem.components.charts

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.OutgoTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor

/**
 * A highly customizable, animated Donut Chart.
 * Useful for displaying proportions, progress, or distribution without relying on specific business logic.
 *
 * @param progress The fill percentage of the active arc, between 0.0f and 1.0f.
 * @param activeColor The color of the filled progress arc.
 * @param modifier The layout modifier. The chart will attempt to be perfectly circular.
 * @param trackColor The color of the background ring. Defaults to a subtle version of the active color.
 * @param strokeWidth The thickness of the donut ring.
 * @param centerTitle Optional large text to display in the middle of the donut.
 * @param centerSubtitle Optional smaller text to display below the title.
 */
@Composable
fun AppDonutChart(
    progress: Float,
    activeColor: Color,
    modifier: Modifier = Modifier,
    trackColor: Color = activeColor.copy(alpha = 0.15f),
    strokeWidth: Dp = 16.dp,
    startAngle: Float = -90f,
    centerTitle: String? = null,
    centerSubtitle: String? = null
) {


    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 1000),
        label = "donutProgress"
    )

    Box(
        modifier = modifier.aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(strokeWidth / 2)
        ) {
            val sweepAngle = animatedProgress * 360f

            drawArc(
                color = trackColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )

            drawArc(
                color = activeColor,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Butt)
            )
        }

        // 3. Conditionally render the center labels
        if (centerTitle != null || centerSubtitle != null) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (centerTitle != null) {
                    Text(
                        text = centerTitle,
                        style = AppTheme.typo.subtitle,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.colors.textPrimary.toColor()
                    )
                }
                if (centerSubtitle != null) {
                    Text(
                        text = centerSubtitle,
                        style = AppTheme.typo.caption,
                        color = AppTheme.colors.textSecondary.toColor()
                    )
                }
            }
        }
    }
}

// --- PREVIEWS ---

@Preview(showBackground = true, name = "Donut - Complete with text")
@Composable
fun PreviewDonutChartComplete() {
    OutgoTheme {
        AppDonutChart(
            progress = 0.65f, // 65%
            activeColor = AppTheme.colors.primary.toColor(),
            modifier = Modifier.width(200.dp),
            centerTitle = "65%",
            centerSubtitle = "Dépensé"
        )
    }
}

@Preview(showBackground = true, name = "Donut - Visual Only (Thin)")
@Composable
fun PreviewDonutChartVisualOnly() {
    OutgoTheme {
        AppDonutChart(
            progress = 0.30f, // 30%
            activeColor = AppTheme.colors.tertiary.toColor(),
            strokeWidth = 8.dp, // Much thinner ring
            modifier = Modifier.width(100.dp)
            // No text parameters passed!
        )
    }
}