package fr.abknative.outgo.android.app.shell.overlay

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import fr.abknative.outgo.android.app.shell.ShellLabels
import fr.abknative.outgo.android.core.components.buttons.AppButton
import fr.abknative.outgo.android.core.components.buttons.AppTextButton
import fr.abknative.outgo.android.core.designsystem.AppText
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor

@Composable
fun ConflictContent(
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier.padding(AppTheme.dimens.large),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Title ---
        Text(
            text = ShellLabels.CONFLICT_TITLE,
            style = AppTheme.typo.title,
            color = AppTheme.colors.primary.toColor(),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.large))

        // --- Main Explanation ---
        AppText(
            text = ShellLabels.CONFLICT_DESC,
            color = AppTheme.colors.textSecondary.toColor(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.large))

        // --- The "Question" ---
        AppText(
            text = ShellLabels.CONFLICT_QUESTION,
            style = AppTheme.typo.caption,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.extraLarge))

        // --- Primary Action ---
        AppButton(
            onClick = onConfirm,
            modifier = Modifier.fillMaxWidth()
        ) {
            AppText(
                text = ShellLabels.CONFLICT_CONFIRM,
                color = AppTheme.colors.textOnBrand.toColor()
            )
        }

        Spacer(modifier = Modifier.height(AppTheme.dimens.small))

        // --- Secondary Action ---
        AppTextButton(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth()
        ) {
            AppText(text = ShellLabels.CONFLICT_CANCEL)
        }
    }
}