package fr.abknative.outgo.android.designsystem.components.feedback

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import fr.abknative.outgo.android.designsystem.components.buttons.AppButton
import fr.abknative.outgo.android.designsystem.components.buttons.AppOutlinedButton
import fr.abknative.outgo.android.designsystem.components.cards.GlassCard
import fr.abknative.outgo.android.designsystem.foundation.AppBackground
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.OutgoTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor

/**
 * A generic, reusable confirmation dialog wrapped in a [GlassCard].
 * Maintains the application's consistent UI style while offering 50/50 weighted
 * slots for action buttons.
 *
 * @param title The title of the dialog.
 * @param description The body text explaining the action or consequence.
 * @param onDismiss Callback invoked when the user dismisses the dialog.
 * @param confirmButton A composable slot for the primary action (e.g., [AppButton]).
 * @param dismissButton An optional composable slot for the secondary action (e.g., [AppOutlinedButton]).
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
        // We use the default GlassCard padding or override it here
        GlassCard {
            Column(
                modifier = Modifier.padding(AppTheme.dimens.large),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = AppTheme.typo.subtitle,
                    color = AppTheme.colors.primary.toColor(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(AppTheme.dimens.large))

                Text(
                    text = description,
                    style = AppTheme.typo.caption,
                    color = AppTheme.colors.textPrimary.toColor(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(AppTheme.dimens.extraLarge))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (dismissButton != null) {
                        Box(modifier = Modifier.weight(1f)) {
                            dismissButton()
                        }
                        Spacer(modifier = Modifier.width(AppTheme.dimens.medium))
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        confirmButton()
                    }
                }
            }
        }
    }
}

// --- PREVIEWS ---

@Preview(showBackground = true, name = "Confirmation Dialog - Design System")
@Composable
fun PreviewConfirmationDialog() {
    OutgoTheme {
        // Mocking the background to see the GlassCard effect
        AppBackground {
            ConfirmationDialog(
                title = "Supprimer l'opération ?",
                description = "Cette action est irréversible. Êtes-vous sûr de vouloir continuer ?",
                onDismiss = {},
                dismissButton = {
                    AppOutlinedButton(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Annuler")
                    }
                },
                confirmButton = {
                    AppButton(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Supprimer")
                    }
                }
            )
        }
    }
}