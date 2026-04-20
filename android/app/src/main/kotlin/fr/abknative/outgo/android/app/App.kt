package fr.abknative.outgo.android.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.abknative.outgo.android.app.shell.ShellScreen
import fr.abknative.outgo.android.core.designsystem.OutgoTheme
import fr.abknative.outgo.core.api.nav.NavCoordinator
import fr.abknative.outgo.shell.api.ShellIntent
import fr.abknative.outgo.shell.api.ShellPresenter
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun App() {

    val coordinator: NavCoordinator = koinInject()
    val shellPresenter: ShellPresenter = koinViewModel()

    val shellState by shellPresenter.state.collectAsStateWithLifecycle()
    val systemTheme = isSystemInDarkTheme()

    LaunchedEffect(Unit) {
        shellPresenter.onIntent(ShellIntent.InitTheme(systemTheme))
    }

    val isDarkMode = if (shellState.isThemeInitialized) shellState.isDarkMode else systemTheme

    OutgoTheme(darkTheme = isDarkMode) {
        ShellScreen(
            shellPresenter = shellPresenter,
            coordinator = coordinator,
            isDarkMode = isDarkMode,
            onToggleDarkMode = { newThemeValue ->
                shellPresenter.onIntent(ShellIntent.UpdateDarkMode(newThemeValue))
            }
        )
    }
}