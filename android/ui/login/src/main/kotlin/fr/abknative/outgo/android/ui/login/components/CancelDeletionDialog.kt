package fr.abknative.outgo.android.ui.login.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import fr.abknative.outgo.android.core.designsystem.AppText

@Composable
fun CancelDeletionDialog(
    onDismiss: () -> Unit,
    onConfirmQuit: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { AppText(text = "Annuler la suppression ?") },
        text = {
            AppText(text = "Vos données ont déjà été effacées du serveur. Si vous annulez, votre compte existera toujours mais sera vide. Vous pourrez le supprimer plus tard depuis les paramètres.")
        },
        confirmButton = {
            TextButton(onClick = {
                onDismiss()
                onConfirmQuit()
            }) { AppText(text = "Quitter") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { AppText(text = "Continuer la suppression") }
        }
    )
}