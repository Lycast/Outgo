package fr.abknative.outgo.android.components.common

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import fr.abknative.outgo.android.R
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
                painter = painterResource(R.drawable.cloud_check),
                contentDescription = AccessibilityLabels.SYNCED,
                tint = MaterialTheme.colorScheme.primary
            )
        } else {
            Icon(
                painter = painterResource(R.drawable.cloud_slash),
                contentDescription = AccessibilityLabels.NOT_SYNCED,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}