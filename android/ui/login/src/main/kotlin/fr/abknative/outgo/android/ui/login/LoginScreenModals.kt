package fr.abknative.outgo.android.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import fr.abknative.outgo.android.core.components.cards.GlassCard
import fr.abknative.outgo.android.core.designsystem.AppTheme
import fr.abknative.outgo.android.ui.login.login.ConflictContent
import fr.abknative.outgo.login.api.LoginIntent
import fr.abknative.outgo.login.api.LoginPresenter

@Composable
fun LoginScreenModals(
    showConflictDialog: Boolean,
    presenter: LoginPresenter
) {
    if (showConflictDialog) {
        Dialog(onDismissRequest = { presenter.onIntent(LoginIntent.CancelConflict) }) {
            GlassCard {
                Column(
                    modifier = Modifier.padding(AppTheme.dimens.large),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ConflictContent(
                        onConfirm = { presenter.onIntent(LoginIntent.ResolveConflict) },
                        onCancel = { presenter.onIntent(LoginIntent.CancelConflict) }
                    )
                }
            }
        }
    }
}