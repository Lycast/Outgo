package fr.abknative.outgo.android.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CloudSync
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
                imageVector = Icons.Rounded.CloudSync,
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