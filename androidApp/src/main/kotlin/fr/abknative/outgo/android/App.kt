package fr.abknative.outgo.android

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import fr.abknative.outgo.android.screens.DashboardScreen
import fr.abknative.outgo.android.screens.SettingsScreen
import fr.abknative.outgo.android.ui.SettingsLabels
import fr.abknative.outgo.android.ui.theme.OutgoTheme
import fr.abknative.outgo.app.nav.AppCoordinator
import fr.abknative.outgo.app.nav.AppStep
import fr.abknative.outgo.core.api.KeyValueStorage
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun App() {

    val coordinator: AppCoordinator = koinInject()
    val storage: KeyValueStorage = koinInject()
    val uriHandler = LocalUriHandler.current

    val navState by coordinator.state.collectAsState()

    BackHandler(enabled = navState.canGoBack) {
        coordinator.handleBack()
    }

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

            AnimatedContent(
                targetState = navState.currentStep,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "AppNav"
            ) { step ->
                when (step) {
                    AppStep.Dashboard -> {
                        DashboardScreen(
                            presenter = koinViewModel(),
                            onNavigateToSettings = {
                                coordinator.navigateTo(AppStep.Settings)
                            }
                        )
                    }

                    AppStep.Settings -> {
                        SettingsScreen(
                            onNavigateBack = { coordinator.handleBack() },
                            isDarkMode = isDarkMode,
                            onToggleDarkMode = { newThemeValue ->
                                isDarkMode = newThemeValue
                                storage.putBoolean(themeKey, newThemeValue)
                            },
                            onCoffeeClick = { uriHandler.openUri(SettingsLabels.URL_COFFEE) },
                            onTipsClick = { uriHandler.openUri(SettingsLabels.URL_SITE) },
                            onContactClick = { uriHandler.openUri(SettingsLabels.URL_CONTACT) }
                        )
                    }
                }
            }
        }
    }
}