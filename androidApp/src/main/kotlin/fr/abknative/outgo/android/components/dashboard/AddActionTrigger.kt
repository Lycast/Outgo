package fr.abknative.outgo.android.components.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import fr.abknative.outgo.android.ui.AccessibilityLabels

@Composable
fun AddActionTrigger(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = AccessibilityLabels.ADD_EXPENSE
        )
    }
}