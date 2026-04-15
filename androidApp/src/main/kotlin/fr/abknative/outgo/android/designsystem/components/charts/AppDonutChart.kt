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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.core.ui.DesignAnimations

@Composable
fun AppDonutChart(
    modifier: Modifier = Modifier,
    progress: Float,
    activeColor: Color,
    isMirrored: Boolean = false,
    trackColor: Color = activeColor.copy(alpha = 0.15f),
    strokeWidth: Dp = 16.dp,
    startAngle: Float = -90f,
    centerTitle: String? = null,
    centerSubtitle: String? = null
) {


    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = DesignAnimations.NORMAL),
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
            val baseSweepAngle = animatedProgress * 360f
            val actualSweepAngle = if (isMirrored) -baseSweepAngle else baseSweepAngle

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
                sweepAngle = actualSweepAngle,
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