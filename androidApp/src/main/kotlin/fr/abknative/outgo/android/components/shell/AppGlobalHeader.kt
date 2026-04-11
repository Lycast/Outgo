package fr.abknative.outgo.android.components.shell

import androidx.compose.runtime.Composable
import fr.abknative.outgo.core.api.nav.AppStep
import fr.abknative.outgo.core.api.nav.NavCoordinator
import fr.abknative.outgo.shell.api.ShellIntent
import fr.abknative.outgo.shell.api.ShellPresenter
import fr.abknative.outgo.shell.api.ShellState

/**
 * Composant privé pour mutualiser l'affichage du Header (Paysage ou Portrait)
 */
@Composable
fun AppGlobalHeader(
    isVertical: Boolean,
    currentStep: AppStep,
    shellPresenter: ShellPresenter,
    shellState: ShellState,
    coordinator: NavCoordinator,
    onShowSyncModal: () -> Unit,
    onShowPremiumTeasing: () -> Unit
) {
    Header(
        syncState = shellState.syncState,
        isVertical = isVertical,
        currentStep = currentStep,
        isPremium = shellState.isPremium,
        onSyncIconClick = {
            if (shellState.syncState.isUnauthenticated) {
                onShowSyncModal()
            } else {
                shellPresenter.onIntent(ShellIntent.RefreshSync)
            }
        },
        onNavigate = { step -> coordinator.navigateTo(step) },
        onTeasingClick = onShowPremiumTeasing
    )
}