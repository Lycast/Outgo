package fr.abknative.outgo.android.core.components.charts

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.core.components.feedback.InfoTooltip
import fr.abknative.outgo.android.core.designsystem.AppText
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor

@Composable
fun AppPairedBar(
    modifier: Modifier = Modifier,
    topLabel: String,
    topAmount: String,
    topProgress: Float,
    topBarColor: Color,
    bottomLabel: String,
    bottomAmount: String,
    bottomProgress: Float,
    bottomBarColor: Color,
    bottomTooltipTitle: String? = null,
    bottomTooltipDesc: String? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.dimens.medium)
    ) {
        // --- TOP TEXT ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            AppText(
                text = topLabel,
                style = AppTheme.typo.caption,
                color = AppTheme.colors.textSecondary.toColor()
            )
            AppText(
                text = topAmount
            )
        }

        // --- SANDWICH BARS ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 1.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {

            val strokeSize = AppTheme.dimens.extraSmall

            AnimatedProgressBar(
                progress = topProgress,
                color = topBarColor.copy(alpha = 0.5f),
                shape = RoundedCornerShape(topStart = strokeSize, topEnd = strokeSize)
            )
            AnimatedProgressBar(
                progress = bottomProgress,
                color = bottomBarColor.copy(alpha = 0.5f),
                shape = RoundedCornerShape(bottomStart = strokeSize, bottomEnd = strokeSize)
            )
        }

        // ---BOTTOM TEXT (With optional Tooltip) ---
        val bottomTextContent = @Composable {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                AppText(
                    text = bottomLabel,
                    style = AppTheme.typo.caption,
                    color = AppTheme.colors.textSecondary.toColor()
                )
                AppText(
                    text = bottomAmount,
                )
            }
        }

        if (bottomTooltipTitle != null && bottomTooltipDesc != null) {
            InfoTooltip(title = bottomTooltipTitle, description = bottomTooltipDesc) {
                bottomTextContent()
            }
        } else {
            bottomTextContent()
        }
    }
}

@Composable
private fun AnimatedProgressBar(
    progress: Float,
    color: Color,
    shape: Shape
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 800),
        label = "barAnimation"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(AppTheme.dimens.extraSmall)
            .background(
                color = AppTheme.colors.textSecondary.toColor().copy(alpha = 0.1f),
                shape = shape
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress)
                .fillMaxHeight()
                .background(color = color, shape = shape)
        )
    }
}