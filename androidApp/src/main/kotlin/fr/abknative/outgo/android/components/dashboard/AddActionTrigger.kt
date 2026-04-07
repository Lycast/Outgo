package fr.abknative.outgo.android.components.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.R
import fr.abknative.outgo.android.components.common.glassEffect
import fr.abknative.outgo.android.ui.AccessibilityLabels
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.OutgoTheme
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
        modifier = modifier.glassEffect(shape = MaterialTheme.shapes.large, borderSize = AppTheme.spacing.medium).size(56.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.plus_bold),
            contentDescription = AccessibilityLabels.ADD_EXPENSE
        )
    }
}


/**
 * Preview for the AddActionTrigger component.
 * Wrapped in [AppTheme] to provide the required CompositionLocal color tokens.
 */
@Preview(showBackground = true, name = "AddActionTrigger - Glass Effect")
@Composable
fun PreviewAddActionTrigger() {
    // 1. On englobe la preview avec le thème de l'application
    OutgoTheme {
        Box(
            modifier = Modifier
                .background(AppTheme.colors.background.toColor()) // Fond sombre pour voir l'effet
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            AddActionTrigger(
                onClick = { /* Preview mock action */ }
            )
        }
    }
}