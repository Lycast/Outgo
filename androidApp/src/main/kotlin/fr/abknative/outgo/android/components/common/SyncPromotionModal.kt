package fr.abknative.outgo.android.components.common

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.ui.HeaderLabels

@Composable
fun SyncPromotionModal(
    onDismiss: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                painter = painterResource(R.drawable.cloud_check),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(text = HeaderLabels.SYNC_PROMO_TITLE)
        },
        text = {
            Text(text = HeaderLabels.SYNC_PROMO_DESC)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismiss()
                    onNavigateToLogin()
                }
            ) {
                Text(HeaderLabels.SYNC_PROMO_ACTION_LOGIN)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(HeaderLabels.SYNC_PROMO_ACTION_LATER)
            }
        }
    )
}