package fr.abknative.outgo.android.core.components.feedback

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.SnackbarData
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import fr.abknative.outgo.android.core.components.cards.GlassCard
import fr.abknative.outgo.android.core.designsystem.AppText
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor

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
                AppText(
                    text = snackbarData.visuals.message,
                    style = AppTheme.typo.label.copy(fontWeight = FontWeight.Medium),
                    modifier = Modifier.weight(1f)
                )

                snackbarData.visuals.actionLabel?.let { action ->
                    AppText(
                        text = action.uppercase(),
                        style = AppTheme.typo.label.copy(fontWeight = FontWeight.Bold),
                        color = AppTheme.colors.primary.toColor(),
                        modifier = Modifier.clickable { snackbarData.performAction() }
                    )
                }
            }
        }
    }
}