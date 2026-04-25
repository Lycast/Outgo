package fr.abknative.outgo.android.app.shell

import androidx.compose.runtime.Composable
import fr.abknative.outgo.android.app.shell.components.SyncPromotionModal
import fr.abknative.outgo.android.app.shell.overlay.GlobalStateOverlay
import fr.abknative.outgo.shell.api.model.ShellOverlayState

@Composable
fun ShellModals(
    showSyncModal: Boolean,
    showPremiumTeasingModal: Boolean,
    overlayState: ShellOverlayState,
    onDismissSync: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onResolveConflictDownloadCloud: () -> Unit,
    onResolveConflictCancel: () -> Unit,
    onRetry: () -> Unit
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

    GlobalStateOverlay(
        overlayState = overlayState,
        onCancel = onResolveConflictCancel,
        onConfirmCloud = onResolveConflictDownloadCloud,
        onRetry = onRetry
    )
}