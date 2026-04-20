package fr.abknative.outgo.android.ui.settings.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import fr.abknative.outgo.android.core.CommonLabels
import fr.abknative.outgo.android.core.DialogLabels
import fr.abknative.outgo.android.core.components.buttons.AppTextButton
import fr.abknative.outgo.android.core.components.buttons.HoldToConfirmButton
import fr.abknative.outgo.android.core.components.cards.GlassCard
import fr.abknative.outgo.android.core.designsystem.AppText
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.core.designsystem.toColor

@Composable
fun DeleteAccountDialog(
    onConfirm: (wipeLocal: Boolean, wipeServer: Boolean, revokeAuth: Boolean) -> Unit,
    onDismiss: () -> Unit
) {

    var wipeLocal by remember { mutableStateOf(false) }
    var wipeServer by remember { mutableStateOf(false) }
    var revokeAuth by remember { mutableStateOf(false) }

    LaunchedEffect(revokeAuth) {
        if (revokeAuth) {
            wipeServer = true
        }
    }

    val canConfirm = wipeLocal || wipeServer || revokeAuth

    Dialog(onDismissRequest = onDismiss) {
        GlassCard {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppText(
                    text = DialogLabels.DELETE_ACCOUNT_TITLE,
                    style = AppTheme.typo.title,
                    color = AppTheme.colors.primary.toColor(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                AppText(
                    text = DialogLabels.DELETE_ACCOUNT_CHOICE_DESC,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.medium)) {
                    DialogSwitchRow(
                        title = DialogLabels.DELETE_ACCOUNT_LOCAL_TITLE,
                        subtitle = DialogLabels.DELETE_ACCOUNT_LOCAL_DESC,
                        isChecked = wipeLocal,
                        onCheckedChange = { wipeLocal = it }
                    )

                    DialogSwitchRow(
                        title = DialogLabels.DELETE_ACCOUNT_SERVER_TITLE,
                        subtitle = DialogLabels.DELETE_ACCOUNT_SERVER_DESC,
                        isChecked = wipeServer,
                        enabled = !revokeAuth,
                        onCheckedChange = { wipeServer = it }
                    )

                    DialogSwitchRow(
                        title = DialogLabels.DELETE_ACCOUNT_AUTH_TITLE,
                        subtitle = DialogLabels.DELETE_ACCOUNT_AUTH_DESC,
                        isChecked = revokeAuth,
                        onCheckedChange = { revokeAuth = it }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppTextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(0.7f).fillMaxWidth()
                    ) { AppText(text = CommonLabels.ACTION_CANCEL) }

                    Spacer(modifier = Modifier.width(AppTheme.dimens.small))

                    HoldToConfirmButton(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        label = CommonLabels.ACTION_DELETE,
                        enabled = canConfirm,
                        onConfirm = { onConfirm(wipeLocal, wipeServer, revokeAuth) }
                    )
                }
            }
        }
    }
}

/**
 * Internal component to draw a compact Switch row adapted for modal views.
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
        Column(modifier = Modifier.weight(1f).padding(end = AppTheme.dimens.small)) {
            AppText(
                text = title
            )
            AppText(
                text = subtitle,
                style = AppTheme.typo.label,
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