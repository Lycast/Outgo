package fr.abknative.outgo.android.components.common

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor
import kotlinx.coroutines.launch


@Composable
fun SecondaryButton(
    label: String,
    labelColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(
            text = label,
            style = AppTheme.typo.label,
            color = labelColor
        )
    }
}


@Composable
fun PrimaryButton(
    label: String,
    labelColor: Color,
    containerColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = 48.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = labelColor
        )
    ) {
        Text(
            text = label,
            style = AppTheme.typo.label,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}


/**
 * A custom button that requires the user to hold it down for a specific duration to trigger the action.
 * Displays a visual progress bar filling up from left to right.
 *
 * @param label The text displayed on the button.
 * @param onConfirm The action triggered when the hold duration is successfully reached.
 * @param durationMillis The time in milliseconds the user must hold the button (default is 1500ms).
 */
@Composable
fun HoldToConfirmButton(
    label: String,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    durationMillis: Int = 2000
) {
    val coroutineScope = rememberCoroutineScope()
    val progress = remember { Animatable(0f) }
    val haptic = LocalHapticFeedback.current
    val containerColor = AppTheme.colors.primary.toColor().copy(alpha = 0.1f)
    val progressColor = AppTheme.colors.error.toColor().copy(alpha = 0.5f)

    Box(
        modifier = modifier
            .defaultMinSize(minHeight = 48.dp)
            .clip(RoundedCornerShape(50))
            .background(containerColor)
            .then(
                if (enabled) {
                    Modifier.pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
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
                                tryAwaitRelease()
                                job.cancel()
                                coroutineScope.launch { progress.snapTo(0f) }
                            }
                        )
                    }
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth(progress.value)
                .background(progressColor)
                .align(Alignment.CenterStart)
        )

        Text(
            text = label,
            style = AppTheme.typo.label,
            color = if(enabled) AppTheme.colors.error.toColor() else AppTheme.colors.textSecondary.toColor().copy(alpha = 0.5f),
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
        )
    }
}