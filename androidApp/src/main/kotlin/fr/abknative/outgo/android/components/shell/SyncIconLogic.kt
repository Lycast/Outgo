package fr.abknative.outgo.android.components.shell

import androidx.compose.animation.core.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.designsystem.components.buttons.AppHeaderButton
import fr.abknative.outgo.android.designsystem.components.feedback.InfoTooltip
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.AccessibilityLabels
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.core.ui.DesignAnimations
import fr.abknative.outgo.sync.api.model.SyncState

@Composable
fun SyncIconLogic(
    syncState: SyncState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "SyncAnimation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (syncState.isInProgress) 360f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = DesignAnimations.SLOW, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "SyncRotation"
    )

    val (iconRes, iconTint, contentDescription) = when (syncState) {
        SyncState.UNAUTHENTICATED -> Triple(
            R.drawable.cloud_slash,
            AppTheme.colors.textSecondary.toColor().copy(alpha = 0.7f),
            AccessibilityLabels.NOT_SYNCED
        )
        SyncState.OFFLINE -> Triple(
            R.drawable.cloud_warning,
            AppTheme.colors.textSecondary.toColor().copy(alpha = 0.7f),
            AccessibilityLabels.SYNC_ERROR
        )
        SyncState.PENDING -> Triple(
            R.drawable.arrows_clockwise,
            AppTheme.colors.primary.toColor(),
            AccessibilityLabels.NOT_SYNCED
        )
        SyncState.IN_PROGRESS -> Triple(
            R.drawable.arrows_clockwise,
            AppTheme.colors.primary.toColor(),
            AccessibilityLabels.LOADING
        )
        SyncState.UP_TO_DATE -> Triple(
            R.drawable.cloud_check,
            AppTheme.colors.primary.toColor(),
            AccessibilityLabels.SYNCED
        )
        SyncState.ERROR -> Triple(
            R.drawable.cloud_warning,
            AppTheme.colors.textSecondary.toColor().copy(alpha = 0.7f),
            AccessibilityLabels.SYNC_ERROR
        )
    }

    val syncIcon = @Composable {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            tint = iconTint,
            modifier = if (syncState.isInProgress) Modifier.rotate(rotation) else Modifier
        )
    }

    if (syncState.isOffline) {
        InfoTooltip(
            title = CommonLabels.SYNC_OFFLINE_TITLE,
            description = CommonLabels.SYNC_OFFLINE_DESC,
            modifier = modifier
        ) {
            AppHeaderButton(onClick = {}) {
                syncIcon()
            }
        }
    } else {
        AppHeaderButton(
            onClick = onClick,
            enabled = !syncState.isInProgress,
            modifier = modifier
        ) {
            syncIcon()
        }
    }
}