package fr.abknative.outgo.android.screens

import androidx.compose.runtime.Composable
import fr.abknative.outgo.android.components.sync.LoadingOverlay
import fr.abknative.outgo.android.components.sync.SyncPromotionModal

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