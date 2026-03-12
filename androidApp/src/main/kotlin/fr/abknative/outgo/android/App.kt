package fr.abknative.outgo.android

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import fr.abknative.outgo.android.screens.DashboardScreen
import fr.abknative.outgo.android.screens.SettingsScreen
import fr.abknative.outgo.android.ui.theme.OutgoTheme
import fr.abknative.outgo.core.api.KeyValueStorage
import fr.abknative.outgo.outgoing.api.presenter.OutgoingPresenter
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject // N'oubliez pas cet import

@Composable
fun App() {

    val presenter: OutgoingPresenter = koinViewModel()
    val storage: KeyValueStorage = koinInject()

    var showSettings by remember { mutableStateOf(false) }

    val systemTheme = isSystemInDarkTheme()
    val themeKey = "app_is_dark_mode"

    var isDarkMode by remember {
        mutableStateOf(storage.getBoolean(themeKey, systemTheme))
    }

    OutgoTheme(darkTheme = isDarkMode) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            if (showSettings) {
                // --- ÉCRAN PARAMÈTRES ---
                SettingsScreen(
                    onNavigateBack = { showSettings = false },
                    isDarkMode = isDarkMode,
                    onToggleDarkMode = { newThemeValue ->
                        // 3. Mise à jour de l'UI ET sauvegarde sur le disque
                        isDarkMode = newThemeValue
                        storage.putBoolean(themeKey, newThemeValue)
                    },
                    onCoffeeClick = { /* TODO: Ouvrir un lien web */ },
                    onTipsClick = { /* TODO: Ouvrir un lien web */ }
                )
            } else {
                // --- ÉCRAN DASHBOARD ---
                DashboardScreen(
                    presenter = presenter,
                    onNavigateToSettings = { showSettings = true }
                )
            }
        }
    }
}