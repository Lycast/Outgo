package fr.abknative.outgo.android

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import fr.abknative.outgo.android.designsystem.foundation.OutgoTheme
import fr.abknative.outgo.android.screens.ShellScreen
import fr.abknative.outgo.core.api.KeyValueStorage
import fr.abknative.outgo.core.api.nav.NavCoordinator
import fr.abknative.outgo.shell.api.ShellPresenter
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun App() {

    val coordinator: NavCoordinator = koinInject()
    val storage: KeyValueStorage = koinInject()
    val shellPresenter: ShellPresenter = koinViewModel()

    val systemTheme = isSystemInDarkTheme()
    val themeKey = "app_is_dark_mode"
    var isDarkMode by remember {
        mutableStateOf(storage.getBoolean(themeKey, systemTheme))
    }

    OutgoTheme(darkTheme = isDarkMode) {
        ShellScreen(
            shellPresenter = shellPresenter,
            coordinator = coordinator,
            isDarkMode = isDarkMode,
            onToggleDarkMode = { newThemeValue ->
                isDarkMode = newThemeValue
                storage.putBoolean(themeKey, newThemeValue)
            }
        )
    }
}