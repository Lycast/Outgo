package fr.abknative.outgo.android.components.common

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.ui.AccessibilityLabels
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor
import fr.abknative.outgo.core.api.model.SyncUiState

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
            AppTheme.colors.textSecondary.toColor().copy(alpha = 0.7f),
            AccessibilityLabels.NOT_SYNCED
        )
        SyncUiState.OFFLINE -> Triple(
            R.drawable.cloud_warning,
            AppTheme.colors.textSecondary.toColor().copy(alpha = 0.7f),
            AccessibilityLabels.SYNC_ERROR
        )
        SyncUiState.PENDING -> Triple(
            R.drawable.arrows_clockwise,
            AppTheme.colors.primary.toColor(),
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
            AppTheme.colors.textSecondary.toColor().copy(alpha = 0.7f),
            AccessibilityLabels.SYNC_ERROR
        )
    }

    // --- GESTION DU TOOLTIP POUR LE MODE HORS-LIGNE ---
    if (syncState.isOffline) {
        InfoTooltip(
            title = "Réseau indisponible", // todo Tu pourras extraire ces textes dans tes Labels
            description = "Vérifiez votre connexion internet. L'application fonctionne normalement hors-ligne, la synchronisation reprendra plus tard.",
            modifier = modifier
        ) {
            Box(
                modifier = Modifier.size(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = contentDescription,
                    tint = iconTint
                )
            }
        }
    } else {
        // --- COMPORTEMENT NORMAL ---
        IconButton(
            onClick = onClick,
            enabled = !syncState.isInProgress,
            modifier = modifier
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = contentDescription,
                tint = iconTint,
                modifier = if (syncState.isInProgress) Modifier.rotate(rotation) else Modifier
            )
        }
    }
}