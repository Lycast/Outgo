package fr.abknative.outgo.android.components.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import fr.abknative.outgo.android.designsystem.components.buttons.AppButton
import fr.abknative.outgo.android.designsystem.components.buttons.AppTextButton
import fr.abknative.outgo.android.designsystem.components.cards.GlassCard
import fr.abknative.outgo.android.designsystem.foundation.AppBackground
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.OutgoTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.android.ui.LoginLabels

/**
 * A specialized dialog shown when a data conflict is detected between
 * the local database and the cloud storage.
 * It uses the [GlassCard] container to maintain UI consistency.
 *
 * @param onConfirm Callback invoked when the user chooses to resolve the conflict (e.g., download cloud data).
 * @param onCancel Callback invoked to dismiss the dialog or keep local state.
 */
@Composable
fun ConflictDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {

    Dialog(onDismissRequest = onCancel) {
        GlassCard {
            Column(
                modifier = Modifier.padding(AppTheme.dimens.large),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // --- Title ---
                Text(
                    text = LoginLabels.CONFLICT_TITLE,
                    style = AppTheme.typo.title,
                    color = AppTheme.colors.primary.toColor(),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(AppTheme.dimens.large))

                // --- Main Explanation ---
                Text(
                    text = LoginLabels.CONFLICT_DESC,
                    style = AppTheme.typo.body,
                    color = AppTheme.colors.textSecondary.toColor(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(AppTheme.dimens.large))

                // --- The "Question" or Call to Action text ---
                Text(
                    text = LoginLabels.CONFLICT_QUESTION,
                    style = AppTheme.typo.caption,
                    color = AppTheme.colors.textPrimary.toColor(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(AppTheme.dimens.extraLarge))

                // --- Primary Action: Confirm/Download ---
                AppButton(
                    onClick = onConfirm,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = LoginLabels.CONFLICT_CONFIRM)
                }

                Spacer(modifier = Modifier.height(AppTheme.dimens.small))

                // --- Secondary Action: Cancel/Keep Local ---
                AppTextButton(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = LoginLabels.CONFLICT_CANCEL)
                }
            }
        }
    }
}

// --- PREVIEWS ---

@Preview(showBackground = true, name = "Conflict Dialog - Refactored")
@Composable
fun PreviewConflictDialog() {
    OutgoTheme {
        // Adding the background to better visualize the Glassmorphism
        AppBackground {
            ConflictDialog(
                onConfirm = {},
                onCancel = {}
            )
        }
    }
}