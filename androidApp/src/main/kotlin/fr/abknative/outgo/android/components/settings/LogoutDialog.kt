package fr.abknative.outgo.android.components.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.outlinedButtonBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

                Button(
                    onClick = onKeepOffline,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppTheme.colors.primary.toColor()
                    )
                ) {
                    Text(DialogLabels.LOGOUT_ACTION_KEEP_BUDGET, style = AppTheme.typo.label)
                }

                OutlinedButton(
                    onClick = onReturnToLocal,
                    modifier = Modifier.fillMaxWidth(),
                    border = outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(AppTheme.colors.primary.toColor())
                    )
                ) {
                    Text(
                        DialogLabels.LOGOUT_ACTION_RETURN_LOCAL,
                        style = AppTheme.typo.label,
                        color = AppTheme.colors.primary.toColor()
                    )
                }

                TextButton(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        CommonLabels.ACTION_CANCEL,
                        style = AppTheme.typo.label,
                        color = AppTheme.colors.textSecondary.toColor()
                    )
                }
            }
        }
    )
}