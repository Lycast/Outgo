package fr.abknative.outgo.android.components.common

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.ui.HeaderLabels
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor

@Composable
fun SyncPromotionModal(
    onDismiss: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AppTheme.colors.surface200.toColor(),
        icon = {
            Icon(
                painter = painterResource(R.drawable.cloud_check),
                contentDescription = null,
                tint = AppTheme.colors.primary.toColor()
            )
        },
        title = {
            Text(
                text = HeaderLabels.SYNC_PROMO_TITLE,
                style = AppTheme.typo.subtitle,
                color = AppTheme.colors.textPrimary.toColor()
            )
        },
        text = {
            Text(
                text = HeaderLabels.SYNC_PROMO_DESC,
                style = AppTheme.typo.body,
                color = AppTheme.colors.textSecondary.toColor()
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismiss()
                    onNavigateToLogin()
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = AppTheme.colors.primary.toColor()
                )
            ) {
                Text(
                    text = HeaderLabels.SYNC_PROMO_ACTION_LOGIN,
                    style = AppTheme.typo.body
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = AppTheme.colors.textSecondary.toColor()
                )
            ) {
                Text(
                    text = HeaderLabels.SYNC_PROMO_ACTION_LATER,
                    style = AppTheme.typo.body
                )
            }
        }
    )
}