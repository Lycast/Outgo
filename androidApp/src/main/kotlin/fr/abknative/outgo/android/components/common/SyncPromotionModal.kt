package fr.abknative.outgo.android.components.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.ui.HeaderLabels
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.OutgoTheme
import fr.abknative.outgo.android.ui.theme.toColor

/**
 * Displays a promotional modal encouraging the offline user to create an account
 * and sync their data to the cloud.
 * * Uses a custom [Dialog] with a [GlassCard] background to maintain UI consistency
 * with other application dialogs.
 *
 * @param onDismiss Callback invoked when the user dismisses the dialog.
 * @param onNavigateToLogin Callback invoked when the user agrees to log in or sign up.
 */
@Composable
fun SyncPromotionModal(
    onDismiss: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        GlassCard {
            Column(
                modifier = Modifier.padding(AppTheme.spacing.large),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(R.drawable.cloud_check),
                    contentDescription = null,
                    tint = AppTheme.colors.primary.toColor(),
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = HeaderLabels.SYNC_PROMO_TITLE,
                    style = AppTheme.typo.subtitle,
                    color = AppTheme.colors.primary.toColor(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = HeaderLabels.SYNC_PROMO_DESC,
                    style = AppTheme.typo.label,
                    color = AppTheme.colors.textPrimary.toColor()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Dismiss Action Button
                    TextButton(
                        onClick = onDismiss,
                    ) {
                        Text(
                            text = HeaderLabels.SYNC_PROMO_ACTION_LATER,
                            color = AppTheme.colors.textSecondary.toColor()
                        )
                    }

                    // Primary Action Button
                    OutlinedButton(
                        onClick = {
                            onDismiss()
                            onNavigateToLogin()
                        },
                        border = BorderStroke(
                            width = 1.dp,
                            color = AppTheme.colors.primary.toColor().copy(alpha = 0.5f),
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = AppTheme.colors.primary.toColor(),
                        )
                    ) {
                        Text(
                            text = HeaderLabels.SYNC_PROMO_ACTION_LOGIN,
                            style = AppTheme.typo.caption
                        )
                    }
                }
            }
        }
    }
}

// --- PREVIEWS ---
@Preview(showBackground = true, name = "Sync Promo Modal - Default")
@Composable
fun PreviewSyncPromotionModal() {
    OutgoTheme {
        SyncPromotionModal(
            onDismiss = {},
            onNavigateToLogin = {}
        )
    }
}