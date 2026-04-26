package fr.abknative.outgo.android.ui.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.abknative.outgo.android.core.CommonLabels
import fr.abknative.outgo.android.core.components.buttons.AppButton
import fr.abknative.outgo.android.core.components.buttons.AppOutlinedButton
import fr.abknative.outgo.android.core.components.buttons.AppTextButton
import fr.abknative.outgo.android.core.designsystem.AppText
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor
import fr.abknative.outgo.android.ui.settings.SettingsLabels

@Composable
fun LogoutDialog(
    onKeepOffline: () -> Unit,
    onReturnToLocal: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        containerColor = AppTheme.colors.surface100.toColor(),
        title = {
            AppText(
                text = SettingsLabels.LOGOUT_TITLE,
                style = AppTheme.typo.title
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.small)) {
                AppText(
                    text = SettingsLabels.LOGOUT_DATA_QUESTION,
                    color = AppTheme.colors.textSecondary.toColor()
                )
            }
        },
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.small)
            ) {
                AppButton(
                    onClick = onKeepOffline,
                    modifier = Modifier.fillMaxWidth()
                ) { AppText(
                    text = SettingsLabels.LOGOUT_ACTION_KEEP_BUDGET,
                    color = AppTheme.colors.textOnBrand.toColor()
                ) }
                AppOutlinedButton(
                    onClick = onReturnToLocal,
                    modifier = Modifier.fillMaxWidth()
                ) { AppText(text = SettingsLabels.LOGOUT_ACTION_RETURN_LOCAL) }
                AppTextButton(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth()
                ) { AppText(text = CommonLabels.ACTION_CANCEL) }
            }
        }
    )
}