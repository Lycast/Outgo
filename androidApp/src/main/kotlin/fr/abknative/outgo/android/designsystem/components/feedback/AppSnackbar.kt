package fr.abknative.outgo.android.designsystem.components.feedback

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor

@Composable
fun AppSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(AppTheme.dimens.medium)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = AppTheme.shapes.medium,
            color = AppTheme.colors.surface200.toColor().copy(alpha = 0.9f),
            tonalElevation = 6.dp,
            shadowElevation = 12.dp,
            modifier = Modifier.widthIn(max = 500.dp)
        ) {
            Row(
                modifier = Modifier.padding(
                    horizontal = AppTheme.dimens.large,
                    vertical = AppTheme.dimens.medium
                ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.medium)
            ) {
                Text(
                    text = snackbarData.visuals.message,
                    style = AppTheme.typo.body,
                    color = AppTheme.colors.textPrimary.toColor(),
                    modifier = Modifier.weight(1f)
                )

                snackbarData.visuals.actionLabel?.let { action ->
                    Text(
                        text = action.uppercase(),
                        style = AppTheme.typo.label,
                        color = AppTheme.colors.primary.toColor(),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { snackbarData.performAction() }
                    )
                }
            }
        }
    }
}