package fr.abknative.outgo.android.components.dashboard

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.ui.AccessibilityLabels
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor

@Composable
fun AddActionTrigger(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = AppTheme.colors.primary.toColor(),
        contentColor = AppTheme.colors.textOnBrand.toColor(),
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(R.drawable.plus_bold),
            contentDescription = AccessibilityLabels.ADD_EXPENSE
        )
    }
}