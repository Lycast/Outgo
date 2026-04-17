package fr.abknative.outgo.android.designsystem.components.buttons

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.foundation.AppText
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.core.ui.DesignAnimations
import kotlinx.coroutines.launch

@Composable
fun HoldToConfirmButton(
    label: String,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    durationMillis: Int = DesignAnimations.HOLD_TO_CONFIRM
) {
    val coroutineScope = rememberCoroutineScope()
    val progress = remember { Animatable(0f) }
    val haptic = LocalHapticFeedback.current

    val containerColor = AppTheme.colors.primary.toColor().copy(alpha = 0.1f)
    val progressColor = AppTheme.colors.error.toColor().copy(alpha = 0.5f)

    Box(
        modifier = modifier
            .defaultMinSize(minHeight = 48.dp)
            .clip(AppTheme.shapes.full)
            .background(containerColor)
            .pointerInput(enabled) {
                if (!enabled) return@pointerInput

                awaitEachGesture {
                    awaitFirstDown()

                    val job = coroutineScope.launch {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        progress.animateTo(
                            targetValue = 1f,
                            animationSpec = tween(durationMillis = durationMillis, easing = LinearEasing)
                        )

                        if (progress.value == 1f) {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onConfirm()
                        }
                    }

                    waitForUpOrCancellation()

                    job.cancel()
                    coroutineScope.launch { progress.snapTo(0f) }
                }
            },
        contentAlignment = Alignment.Center
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

        AppText(
            text = label,
            style = AppTheme.typo.label,
            color = if (enabled) AppTheme.colors.error.toColor() else AppTheme.colors.textSecondary.toColor().copy(alpha = 0.5f),
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
        )
    }
}