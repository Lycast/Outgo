package fr.abknative.outgo.android.components.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import fr.abknative.outgo.android.components.common.GlassCard
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.OutgoTheme
import fr.abknative.outgo.android.ui.theme.toColor

@Composable
fun ConflictDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Dialog(onDismissRequest = onCancel) {
        GlassCard {
            Column(
                modifier = Modifier.padding(AppTheme.spacing.large),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Sauvegarde existante", // TODO: À extraire dans LoginLabels
                    style = AppTheme.typo.title,
                    color = AppTheme.colors.primary.toColor(),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Ce compte contient déjà des données sur nos serveurs. " +
                        "En vous connectant, vos données actuelles seront mises de côté " +
                        "pour afficher celles de votre compte en ligne.")

                Spacer(modifier = Modifier.height(AppTheme.spacing.large))

                Text(
                    text = "Que voulez-vous faire ?",
                    color = AppTheme.colors.primary.toColor(),
                    style = AppTheme.typo.caption,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(AppTheme.spacing.large))

                // Bouton : Option A (Fusion)
                Button(
                    onClick = onConfirm,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primary.toColor())
                ) {
                    Text("Télécharger le Cloud")
                }


                Spacer(modifier = Modifier.height(AppTheme.spacing.medium))

                // Bouton : Annuler
                TextButton(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Annuler la connexion",
                        color = AppTheme.colors.textSecondary.toColor())
                }
            }
        }
    }
}

// --- PREVIEWS ---

@Preview(showBackground = true, name = "Conflict Dialog - Default")
@Composable
fun PreviewConflictDialog() {
    OutgoTheme {
        ConflictDialog(
            onConfirm = {},
            onCancel = {}
        )
    }
}