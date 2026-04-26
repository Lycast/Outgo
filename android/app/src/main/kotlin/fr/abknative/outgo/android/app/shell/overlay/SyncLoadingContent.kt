package fr.abknative.outgo.android.app.shell.overlay

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import fr.abknative.outgo.android.app.shell.ShellLabels
import fr.abknative.outgo.android.core.components.buttons.AppTextButton
import fr.abknative.outgo.android.core.designsystem.AppText
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor

@Composable
fun SyncLoadingContent(onCancel: () -> Unit) {
    Column(
        modifier = Modifier.padding(AppTheme.dimens.extraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = AppTheme.colors.primary.toColor())

        Spacer(modifier = Modifier.height(AppTheme.dimens.large))

        AppText(
            text = ShellLabels.SYNC_LOADING_TITLE,
            style = AppTheme.typo.subtitle,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

        AppText(
            text = ShellLabels.SYNC_LOADING_MESSAGE,
            color = AppTheme.colors.textSecondary.toColor(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.extraLarge))

        AppTextButton(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth()
        ) {
            AppText(text = ShellLabels.CONFLICT_CANCEL)
        }
    }
}