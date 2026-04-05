package fr.abknative.outgo.android.components.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import fr.abknative.outgo.android.ui.CommonLabels
import fr.abknative.outgo.android.ui.DialogLabels
import fr.abknative.outgo.android.ui.theme.AppTheme
import fr.abknative.outgo.android.ui.theme.toColor

@Composable
fun DeleteAccountDialog(
    onConfirm: (wipeLocal: Boolean, wipeServer: Boolean, revokeAuth: Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    // États initiaux de nos 3 niveaux de suppression
    var wipeLocal by remember { mutableStateOf(false) }
    var wipeServer by remember { mutableStateOf(true) }
    var revokeAuth by remember { mutableStateOf(true) }

    LaunchedEffect(revokeAuth) {
        if (revokeAuth) {
            wipeServer = true
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AppTheme.colors.surface200.toColor(),
        title = {
            Text(
                text = DialogLabels.DELETE_ACCOUNT_TITLE,
                style = AppTheme.typo.subtitle,
                color = AppTheme.colors.textPrimary.toColor()
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.medium)) {
                Text(
                    text = DialogLabels.DELETE_ACCOUNT_CHOICE_DESC,
                    style = AppTheme.typo.body,
                    color = AppTheme.colors.textSecondary.toColor()
                )

                Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.small)) {

                    // Niveau 1 : Local
                    DialogSwitchRow(
                        title = DialogLabels.DELETE_ACCOUNT_LOCAL_TITLE,
                        subtitle = DialogLabels.DELETE_ACCOUNT_LOCAL_DESC,
                        isChecked = wipeLocal,
                        onCheckedChange = { wipeLocal = it }
                    )

                    // Niveau 2 : Serveur (Désactivé si Firebase est coché)
                    DialogSwitchRow(
                        title = DialogLabels.DELETE_ACCOUNT_SERVER_TITLE,
                        subtitle = DialogLabels.DELETE_ACCOUNT_SERVER_DESC,
                        isChecked = wipeServer,
                        enabled = !revokeAuth,
                        onCheckedChange = { wipeServer = it }
                    )

                    // Niveau 3 : Profil / Firebase
                    DialogSwitchRow(
                        title = DialogLabels.DELETE_ACCOUNT_AUTH_TITLE,
                        subtitle = DialogLabels.DELETE_ACCOUNT_AUTH_DESC,
                        isChecked = revokeAuth,
                        onCheckedChange = { revokeAuth = it }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(wipeLocal, wipeServer, revokeAuth) },
                colors = ButtonDefaults.textButtonColors(contentColor = AppTheme.colors.error.toColor())
            ) {
                Text(text = DialogLabels.DELETE_ACCOUNT_CONFIRM, style = AppTheme.typo.label)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = AppTheme.colors.textSecondary.toColor())
            ) {
                Text(text = CommonLabels.ACTION_CANCEL, style = AppTheme.typo.label)
            }
        }
    )
}

/**
 * Composant interne pour dessiner une ligne Switch compacte adaptée aux modales.
 */
@Composable
private fun DialogSwitchRow(
    title: String,
    subtitle: String,
    isChecked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (enabled) 1f else 0.5f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = AppTheme.spacing.small)) {
            Text(
                text = title,
                style = AppTheme.typo.body,
                fontWeight = FontWeight.Medium,
                color = AppTheme.colors.textPrimary.toColor()
            )
            Text(
                text = subtitle,
                style = AppTheme.typo.caption,
                color = AppTheme.colors.textSecondary.toColor()
            )
        }
        Switch(
            checked = isChecked,
            onCheckedChange = if (enabled) onCheckedChange else null,
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedThumbColor = AppTheme.colors.surface50.toColor(),
                checkedTrackColor = AppTheme.colors.primary.toColor(),
                uncheckedThumbColor = AppTheme.colors.textSecondary.toColor(),
                uncheckedTrackColor = AppTheme.colors.surface100.toColor()
            )
        )
    }
}