package fr.abknative.outgo.android.app.shell

import androidx.compose.runtime.Composable
import fr.abknative.outgo.android.app.shell.components.LoadingOverlay
import fr.abknative.outgo.android.app.shell.components.SyncPromotionModal

@Composable
fun ShellModals(
    showSyncModal: Boolean,
    showPremiumTeasingModal: Boolean,
    showLoadingOverlay: Boolean,
    onDismissSync: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    if (showSyncModal) {
        SyncPromotionModal(
            onDismiss = onDismissSync,
            onNavigateToLogin = onNavigateToLogin
        )
    }

    if (showPremiumTeasingModal) {
        // PremiumTeasingModal(onDismiss = onDismissPremium)
    }

    if (showLoadingOverlay) {
        LoadingOverlay()
    }
}