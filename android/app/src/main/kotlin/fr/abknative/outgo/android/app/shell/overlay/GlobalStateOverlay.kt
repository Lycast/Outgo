package fr.abknative.outgo.android.app.shell.overlay

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import fr.abknative.outgo.android.core.components.cards.GlassCard
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.shell.api.model.ShellOverlayState

@Composable
fun GlobalStateOverlay(
    overlayState: ShellOverlayState,
    onCancel: () -> Unit,
    onConfirmCloud: () -> Unit,
    onRetry: () -> Unit
) {
    if (overlayState == ShellOverlayState.NONE) return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {}
            )
            .padding(AppTheme.dimens.large),
        contentAlignment = Alignment.Center
    ) {
        GlassCard {
            AnimatedContent(
                targetState = overlayState,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "OverlayTransition"
            ) { state ->
                when (state) {
                    ShellOverlayState.LOADING -> {
                        SyncLoadingContent(onCancel = onCancel)
                    }
                    ShellOverlayState.CONFLICT -> {
                        ConflictContent(
                            onConfirm = onConfirmCloud,
                            onCancel = onCancel
                        )
                    }
                    ShellOverlayState.ERROR -> SyncErrorContent(onRetry = onRetry, onCancel = onCancel) // ✨ NOUVEAU
                    ShellOverlayState.NONE -> Unit
                }
            }
        }
    }
}