package fr.abknative.outgo.android.app.shell.overlay

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import fr.abknative.outgo.android.core.CommonLabels
import fr.abknative.outgo.android.core.LoginLabels
import fr.abknative.outgo.android.core.ShellLabels
import fr.abknative.outgo.android.core.components.buttons.AppButton
import fr.abknative.outgo.android.core.components.buttons.AppTextButton
import fr.abknative.outgo.android.core.designsystem.AppText
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor

@Composable
fun SyncErrorContent(onRetry: () -> Unit, onCancel: () -> Unit) {
    Column(
        modifier = Modifier.padding(AppTheme.dimens.extraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        AppText(
            text = ShellLabels.SYNC_ERROR_TITLE,
            style = AppTheme.typo.subtitle,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.medium))

        AppText(
            text = ShellLabels.SYNC_ERROR_MESSAGE,
            color = AppTheme.colors.textSecondary.toColor(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.extraLarge))

        AppButton(
            onClick = onRetry,
            modifier = Modifier.fillMaxWidth()
        ) {
            AppText(text = CommonLabels.ACTION_RETRY, color = AppTheme.colors.textOnBrand.toColor())
        }

        Spacer(modifier = Modifier.height(AppTheme.dimens.small))

        AppTextButton(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth()
        ) {
            AppText(text = LoginLabels.CONFLICT_CANCEL)
        }
    }
}