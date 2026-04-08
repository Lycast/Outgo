package fr.abknative.outgo.android.components.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
    onMerge: () -> Unit,
    onDiscardLocal: () -> Unit,
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

                Text(
                    text = "Ce compte possède déjà des données sur le cloud.\n\n" +
                            "Choix 1 : Fusionner vos données actuel avec celles du cloud\n\n" +
                            "Choix 2 : Télécharger les données du cloud mais vous perdrez vos données actuel",
                    color = AppTheme.colors.textPrimary.toColor(),
                    style = AppTheme.typo.label
                )

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
                    onClick = onMerge,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primary.toColor())
                ) {
                    Text("Fusionner avec le cloud")
                }


                Spacer(modifier = Modifier.height(AppTheme.spacing.medium))

                // Bouton : Option B (Écraser)
                OutlinedButton(
                    onClick = onDiscardLocal,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Télécharger le Cloud",
                        color = AppTheme.colors.textPrimary.toColor())
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
            onMerge = {},
            onDiscardLocal = {},
            onCancel = {}
        )
    }
}