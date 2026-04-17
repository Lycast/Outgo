package fr.abknative.outgo.android.components.sync

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.designsystem.components.buttons.AppOutlinedButton
import fr.abknative.outgo.android.designsystem.components.buttons.AppTextButton
import fr.abknative.outgo.android.designsystem.components.cards.GlassCard
import fr.abknative.outgo.android.designsystem.foundation.AppText
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.HeaderLabels

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

                AppText(
                    text = HeaderLabels.SYNC_PROMO_TITLE,
                    style = AppTheme.typo.subtitle,
                    color = AppTheme.colors.primary.toColor(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

                AppText(
                    text = HeaderLabels.SYNC_PROMO_DESC,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(AppTheme.dimens.large))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(0.7f)) {
                        AppTextButton(
                            onClick = onDismiss,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AppText(text = HeaderLabels.SYNC_PROMO_ACTION_LATER)
                        }
                    }

                    Spacer(modifier = Modifier.width(AppTheme.dimens.medium))

                    Box(modifier = Modifier.weight(1f)) {
                        AppOutlinedButton(
                            onClick = {
                                onDismiss()
                                onNavigateToLogin()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AppText(text = HeaderLabels.SYNC_PROMO_ACTION_LOGIN)
                        }
                    }
                }
            }
        }
    }
}