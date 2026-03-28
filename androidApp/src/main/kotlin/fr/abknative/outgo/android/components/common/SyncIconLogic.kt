package fr.abknative.outgo.android.components.common

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.ui.AccessibilityLabels
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor
import fr.abknative.outgo.outgoing.api.presenter.SyncUiState

@Composable
fun SyncIconLogic(
    syncState: SyncUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val infiniteTransition = rememberInfiniteTransition(label = "SyncAnimation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (syncState.isInProgress) 360f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "SyncRotation"
    )

    val (iconRes, iconTint, contentDescription) = when (syncState) {
        SyncUiState.UNAUTHENTICATED -> Triple(
            R.drawable.cloud_slash,
            AppTheme.colors.textPrimary.toColor(),
            AccessibilityLabels.NOT_SYNCED
        )
        SyncUiState.IN_PROGRESS -> Triple(
            R.drawable.arrows_clockwise,
            AppTheme.colors.primary.toColor(),
            AccessibilityLabels.LOADING
        )
        SyncUiState.UP_TO_DATE -> Triple(
            R.drawable.cloud_check,
            AppTheme.colors.primary.toColor(),
            AccessibilityLabels.SYNCED
        )
        SyncUiState.ERROR -> Triple(
            R.drawable.cloud_warning,
            AppTheme.colors.error.toColor().copy(alpha = 0.7f),
            AccessibilityLabels.SYNC_ERROR
        )
    }

    IconButton(
        onClick = onClick,
        enabled = !syncState.isInProgress,
        modifier = modifier
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = contentDescription,
                tint = iconTint,
                modifier = if (syncState.isInProgress) Modifier.rotate(rotation) else Modifier
            )
        }
    }
}