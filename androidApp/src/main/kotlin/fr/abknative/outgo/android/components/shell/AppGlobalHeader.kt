package fr.abknative.outgo.android.components.shell

import androidx.compose.runtime.Composable
import fr.abknative.outgo.shell.api.ShellIntent
import fr.abknative.outgo.shell.api.ShellPresenter
import fr.abknative.outgo.shell.api.ShellState

/**
 * Private component to centralize Header display (Landscape or Portrait).
 * Now focused on global utilities: Sync status and Settings access.
 */
@Composable
fun AppGlobalHeader(
    isVertical: Boolean,
    shellPresenter: ShellPresenter,
    shellState: ShellState,
    onShowSyncModal: () -> Unit
) {
    Header(
        syncState = shellState.syncState,
        isVertical = isVertical,
        onSyncIconClick = {
            if (shellState.syncState.isUnauthenticated) {
                onShowSyncModal()
            } else {
                shellPresenter.onIntent(ShellIntent.RefreshSync)
            }
        }
    )
}