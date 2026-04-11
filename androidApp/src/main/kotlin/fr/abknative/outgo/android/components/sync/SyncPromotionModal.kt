package fr.abknative.outgo.android.components.sync

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.designsystem.components.buttons.AppOutlinedButton
import fr.abknative.outgo.android.designsystem.components.buttons.AppTextButton
import fr.abknative.outgo.android.designsystem.components.cards.GlassCard
import fr.abknative.outgo.android.designsystem.foundation.AppBackground
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.OutgoTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.HeaderLabels

/**
 * Displays a promotional modal encouraging the offline user to create an account
 * and sync their data to the cloud.
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
                modifier = Modifier.padding(AppTheme.dimens.large),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(R.drawable.cloud_check),
                    contentDescription = null,
                    tint = AppTheme.colors.primary.toColor(),
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

                Text(
                    text = HeaderLabels.SYNC_PROMO_TITLE,
                    style = AppTheme.typo.subtitle,
                    color = AppTheme.colors.primary.toColor(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

                Text(
                    text = HeaderLabels.SYNC_PROMO_DESC,
                    style = AppTheme.typo.label,
                    color = AppTheme.colors.textPrimary.toColor(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(AppTheme.dimens.large))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Dismiss Action
                    Box(modifier = Modifier.weight(0.75f)) {
                        AppTextButton(
                            onClick = onDismiss,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = HeaderLabels.SYNC_PROMO_ACTION_LATER)
                        }
                    }

                    Spacer(modifier = Modifier.width(AppTheme.dimens.medium))

                    // Primary Action (Login/Signup)
                    Box(modifier = Modifier.weight(1f)) {
                        AppOutlinedButton(
                            onClick = {
                                onDismiss()
                                onNavigateToLogin()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = HeaderLabels.SYNC_PROMO_ACTION_LOGIN)
                        }
                    }
                }
            }
        }
    }
}

// --- PREVIEWS ---

@Preview(showBackground = true, name = "Sync Promo Modal - Refactored")
@Composable
fun PreviewSyncPromotionModal() {
    OutgoTheme {
        AppBackground {
            SyncPromotionModal(
                onDismiss = {},
                onNavigateToLogin = {}
            )
        }
    }
}