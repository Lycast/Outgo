package fr.abknative.outgo.android.components.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.outlinedButtonBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.abknative.outgo.android.ui.DialogLabels
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor

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
            Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.small)) {
                Text(
                    text = "Que souhaitez-vous faire de vos données actuelles ?",
                    style = AppTheme.typo.body,
                    color = AppTheme.colors.textSecondary.toColor()
                )
            }
        },
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.small)
            ) {
                // CHOIX 1 : Conserver la session actuelle en mode hors-ligne
                Button(
                    onClick = onKeepOffline,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppTheme.colors.primary.toColor()
                    )
                ) {
                    Text("Conserver ce budget", style = AppTheme.typo.label)
                }

                // CHOIX 2 : Revenir à l'ancienne session locale
                OutlinedButton(
                    onClick = onReturnToLocal,
                    modifier = Modifier.fillMaxWidth(),
                    border = outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(AppTheme.colors.primary.toColor())
                    )
                ) {
                    Text(
                        "Retrouver mon budget local",
                        style = AppTheme.typo.label,
                        color = AppTheme.colors.primary.toColor()
                    )
                }

                // CHOIX 3 : Annuler
                TextButton(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Annuler",
                        style = AppTheme.typo.label,
                        color = AppTheme.colors.textSecondary.toColor()
                    )
                }
            }
        }
    )
}