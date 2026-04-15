package fr.abknative.outgo.android.components.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import fr.abknative.outgo.android.designsystem.components.buttons.AppButton
import fr.abknative.outgo.android.designsystem.components.buttons.AppTextButton
import fr.abknative.outgo.android.designsystem.components.cards.GlassCard
import fr.abknative.outgo.android.designsystem.foundation.AppTheme
import fr.abknative.outgo.android.designsystem.foundation.toColor
import fr.abknative.outgo.login.api.PostLoginStep

@Composable
fun PostLoginDialog(
    step: PostLoginStep,
    onResolveConflict: () -> Unit,
    onCancel: () -> Unit,
    onRetry: () -> Unit,
    errorMessage: String? = null
) {
    if (step == PostLoginStep.NONE) return

    Dialog(onDismissRequest = { if(step == PostLoginStep.ERROR || step == PostLoginStep.CONFLICT) onCancel() }) {
        GlassCard {
            Column(
                modifier = Modifier.padding(AppTheme.dimens.large),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (step) {
                    PostLoginStep.CONFLICT -> {
                        ConflictContent(onConfirm = onResolveConflict, onCancel = onCancel)
                    }
                    PostLoginStep.SYNCING -> {
                        Text(
                            text = "Synchronisation...",
                            style = AppTheme.typo.title,
                            color = AppTheme.colors.primary.toColor(),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(AppTheme.dimens.large))
                        CircularProgressIndicator(color = AppTheme.colors.primary.toColor())
                        Spacer(Modifier.height(AppTheme.dimens.large))
                        Text(
                            text = "Nous préparons votre budget sécurisé...",
                            style = AppTheme.typo.body,
                            color = AppTheme.colors.textSecondary.toColor(),
                            textAlign = TextAlign.Center
                        )
                    }
                    PostLoginStep.ERROR -> {
                        Text(
                            text = "Oups !",
                            style = AppTheme.typo.title,
                            color = AppTheme.colors.error.toColor(),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(AppTheme.dimens.medium))
                        Text(
                            text = errorMessage ?: "Impossible de récupérer vos données.",
                            style = AppTheme.typo.body,
                            color = AppTheme.colors.textSecondary.toColor(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(AppTheme.dimens.extraLarge))
                        AppButton(onClick = onRetry, modifier = Modifier.fillMaxWidth()) {
                            Text("Réessayer")
                        }
                        Spacer(Modifier.height(AppTheme.dimens.small))
                        AppTextButton(onClick = onCancel, modifier = Modifier.fillMaxWidth()) {
                            Text("Annuler")
                        }
                    }
                }
            }
        }
    }
}

