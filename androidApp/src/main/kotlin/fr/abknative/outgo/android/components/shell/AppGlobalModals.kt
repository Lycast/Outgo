package fr.abknative.outgo.android.components.shell

import androidx.compose.runtime.Composable
import fr.abknative.outgo.android.components.sync.SyncPromotionModal


/**
 * Centralisation des modales globales de l'application
 */
@Composable
fun AppGlobalModals(
    showSyncModal: Boolean,
    showPremiumTeasingModal: Boolean,
    onDismissSync: () -> Unit,
    onDismissPremium: () -> Unit,
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
}