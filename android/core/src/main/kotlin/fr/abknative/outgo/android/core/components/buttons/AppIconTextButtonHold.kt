package fr.abknative.outgo.android.core.components.buttons

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.core.designsystem.AppText
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor
import fr.abknative.outgo.core.ui.DesignAnimations
import kotlinx.coroutines.launch

@Composable
fun AppIconTextButtonHold(
    text: String,
    iconRes: Int,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = AppTheme.colors.error.toColor(),
    durationMillis: Int = DesignAnimations.SLOW
) {
    val coroutineScope = rememberCoroutineScope()
    val progress = remember { Animatable(0f) }
    val haptic = LocalHapticFeedback.current
    val progressColor = tint.copy(alpha = 0.15f)

    Box(
        modifier = modifier
            .height(42.dp)
            .shadow(AppTheme.dimens.extraSmall, AppTheme.shapes.medium)
            .clip(AppTheme.shapes.medium)
            .background(color = AppTheme.colors.surface200.toColor())
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown()
                    val job = coroutineScope.launch {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        progress.animateTo(
                            targetValue = 1f,
                            animationSpec = tween(durationMillis, easing = LinearEasing)
                        )
                        if (progress.value == 1f) {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onConfirm()
                        }
                    }
                    waitForUpOrCancellation()
                    job.cancel()
                    coroutineScope.launch { progress.animateTo(0f) }
                }
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    scaleX = progress.value
                    transformOrigin = TransformOrigin(0f, 0.5f)
                }
                .background(progressColor)
        )

        // Content (Icon + Text)
        Row(
            modifier = Modifier.padding(horizontal = AppTheme.dimens.large),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(AppTheme.dimens.small))
            AppText(
                text = text,
                style = AppTheme.typo.label,
                color = tint
            )
        }
    }
}