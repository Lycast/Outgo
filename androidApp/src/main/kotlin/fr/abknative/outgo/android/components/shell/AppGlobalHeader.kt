package fr.abknative.outgo.android.components.shell

import androidx.compose.runtime.Composable
import fr.abknative.outgo.shell.api.ShellIntent
import fr.abknative.outgo.shell.api.ShellPresenter
import fr.abknative.outgo.shell.api.ShellState

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