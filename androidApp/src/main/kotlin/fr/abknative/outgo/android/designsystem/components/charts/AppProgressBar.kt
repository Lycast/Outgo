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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.foundation.AppTheme

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