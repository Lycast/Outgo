package fr.abknative.outgo.android.components.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.abknative.outgo.android.designsystem.components.buttons.AppButton
import fr.abknative.outgo.android.designsystem.components.buttons.AppOutlinedButton
import fr.abknative.outgo.android.designsystem.components.buttons.AppTextButton
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.android.ui.DialogLabels

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
            Text(
                text = DialogLabels.LOGOUT_TITLE,
                style = AppTheme.typo.title,
                color = AppTheme.colors.textPrimary.toColor()
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.small)) {
                Text(
                    text = DialogLabels.LOGOUT_DATA_QUESTION,
                    style = AppTheme.typo.body,
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
                ) { Text(text = DialogLabels.LOGOUT_ACTION_KEEP_BUDGET) }
                AppOutlinedButton(
                    onClick = onReturnToLocal,
                    modifier = Modifier.fillMaxWidth()
                ) { Text(text = DialogLabels.LOGOUT_ACTION_RETURN_LOCAL) }
                AppTextButton(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth()
                ) { Text(text = CommonLabels.ACTION_CANCEL) }
            }
        }
    )
}