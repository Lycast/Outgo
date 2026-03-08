package fr.abknative.outgo.android.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CloudDone
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.abknative.outgo.android.ui.AccessibilityLabels

@Composable
fun SyncIconLogic(
    isConnected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        if (isConnected) {
            Icon(
                imageVector = Icons.Rounded.CloudDone,
                contentDescription = AccessibilityLabels.SYNCED,
                tint = MaterialTheme.colorScheme.primary
            )
        } else {
            Icon(
                imageVector = Icons.Rounded.CloudOff,
                contentDescription = AccessibilityLabels.NOT_SYNCED,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}