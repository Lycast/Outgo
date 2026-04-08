package fr.abknative.outgo.android.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.OutgoTheme
import fr.abknative.outgo.android.ui.theme.toColor

/**
 * A generic, reusable confirmation dialog wrapped in a custom [GlassCard].
 * Maintains the application's consistent UI style while offering flexibility
 * through custom composable slots for actions.
 *
 * @param title The title of the dialog.
 * @param description The body text explaining the action or consequence.
 * @param onDismiss Callback invoked when the user dismisses the dialog.
 * @param confirmButton A composable slot for the primary action (e.g., a [Button] or [TextButton]).
 * @param dismissButton An optional composable slot for the secondary action.
 */
@Composable
fun ConfirmationDialog(
    title: String,
    description: String,
    onDismiss: () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable (() -> Unit)? = null
) {
    Dialog(onDismissRequest = onDismiss) {
        GlassCard {
            Column(
                modifier = Modifier.padding(AppTheme.spacing.large),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = AppTheme.typo.subtitle,
                    color = AppTheme.colors.primary.toColor(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(AppTheme.spacing.large))

                Text(
                    text = description,
                    style = AppTheme.typo.caption,
                    color = AppTheme.colors.textPrimary.toColor(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(AppTheme.spacing.extraLarge))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (dismissButton != null) {
                        dismissButton()
                        Spacer(modifier = Modifier.width(AppTheme.spacing.medium))
                    }
                    confirmButton()
                }
            }
        }
    }
}

// --- PREVIEWS ---

@Preview(showBackground = true, name = "Confirmation Dialog - Default")
@Composable
fun PreviewConfirmationDialog() {
    OutgoTheme {
        ConfirmationDialog(
            title = "Supprimer l'opération ?",
            description = "Cette action est irréversible. Êtes-vous sûr de vouloir continuer ?",
            onDismiss = {},
            dismissButton = {
                TextButton(onClick = {}) {
                    Text("Annuler", color = AppTheme.colors.textSecondary.toColor())
                }
            },
            confirmButton = {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppTheme.colors.error.toColor()
                    )
                ) {
                    Text("Supprimer")
                }
            }
        )
    }
}