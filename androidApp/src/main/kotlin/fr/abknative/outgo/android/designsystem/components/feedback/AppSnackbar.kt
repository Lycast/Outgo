package fr.abknative.outgo.android.designsystem.components.feedback

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import fr.abknative.outgo.android.designsystem.components.cards.GlassCard
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
        GlassCard(
            backgroundColorA = AppTheme.colors.tertiary.toColor().copy(alpha = 0.1f),
        ) {
            Row(
                modifier = Modifier.padding(
                    horizontal = AppTheme.dimens.large,
                    vertical = AppTheme.dimens.large
                ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.medium)
            ) {
                Text(
                    text = snackbarData.visuals.message,
                    style = AppTheme.typo.label,
                    color = AppTheme.colors.textPrimary.toColor(),
                    fontWeight = FontWeight.Medium,
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