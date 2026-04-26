package fr.abknative.outgo.android.ui.login.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import fr.abknative.outgo.android.core.designsystem.AppText
import fr.abknative.outgo.android.ui.login.LoginLabels

@Composable
fun CancelDeletionDialog(
    onDismiss: () -> Unit,
    onConfirmQuit: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { AppText(text = LoginLabels.DELETE_CANCEL_DIALOG_TITLE) },
        text = {
            AppText(text = LoginLabels.DELETE_CANCEL_DIALOG_DESC)
        },
        confirmButton = {
            TextButton(onClick = {
                onDismiss()
                onConfirmQuit()
            }) { AppText(text = LoginLabels.DELETE_CANCEL_QUIT) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { AppText(text = LoginLabels.DELETE_CANCEL_CONTINUE) }
        }
    )
}