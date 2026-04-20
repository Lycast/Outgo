package fr.abknative.outgo.android.app.shell.components

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
        subtitle = shellState.todayFormatted,
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