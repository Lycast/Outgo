package fr.abknative.outgo.android.components.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor

/**
 * A reusable confirmation dialog for critical or destructive actions.
 *
 * @param title The title of the dialog.
 * @param description The detailed explanation of the action's consequences.
 * @param confirmLabel The text for the confirmation button.
 * @param cancelLabel The text for the cancellation button.
 * @param isDestructive If true, the confirm button will use the error color. Defaults to true.
 * @param onConfirm Callback triggered when the user confirms the action.
 * @param onDismiss Callback triggered when the user cancels or dismisses the dialog.
 */
@Composable
fun ConfirmationDialog(
    title: String,
    description: String,
    confirmLabel: String,
    cancelLabel: String,
    isDestructive: Boolean = true,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AppTheme.colors.surface200.toColor(),
        title = {
            Text(
                text = title,
                style = AppTheme.typo.subtitle,
                color = AppTheme.colors.textPrimary.toColor()
            )
        },
        text = {
            Text(
                text = description,
                style = AppTheme.typo.body,
                color = AppTheme.colors.textSecondary.toColor()
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (isDestructive) AppTheme.colors.error.toColor() else AppTheme.colors.primary.toColor()
                )
            ) {
                Text(text = confirmLabel, style = AppTheme.typo.label)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = AppTheme.colors.textSecondary.toColor())
            ) {
                Text(text = cancelLabel, style = AppTheme.typo.label)
            }
        }
    )
}