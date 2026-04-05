package fr.abknative.outgo.android.components.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor

/**
 * A reusable confirmation dialog for critical or destructive actions.
 * Uses the Slot API to allow flexible button injection.
 *
 * @param title The title of the dialog.
 * @param description The detailed explanation of the action's consequences.
 * @param onDismiss Callback triggered when the user clicks outside the dialog to dismiss it.
 * @param confirmButton The composable for the confirmation action (e.g., PrimaryButton or HoldToConfirmButton).
 * @param dismissButton The composable for the cancellation action (e.g., SecondaryButton). Optional.
 */
@Composable
fun ConfirmationDialog(
    title: String,
    description: String,
    onDismiss: () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable (() -> Unit)? = null
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
        confirmButton = confirmButton,
        dismissButton = dismissButton
    )
}